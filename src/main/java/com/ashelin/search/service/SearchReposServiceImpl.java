package com.ashelin.search.service;

import com.ashelin.search.dto.BranchInfoDto;
import com.ashelin.search.dto.BranchDto;
import com.ashelin.search.dto.RepositoryDto;
import com.ashelin.search.dto.RepositoryInfoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
@Slf4j
public class SearchReposServiceImpl implements SearchReposService {

    private static final String GITHUB_SERVICE = "githubService";
    private final WebClient webClient;

    public SearchReposServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    @Override
    @CircuitBreaker(name = GITHUB_SERVICE, fallbackMethod = "fallbackGetBranches")
    @Retry(name = GITHUB_SERVICE)
    @RateLimiter(name = GITHUB_SERVICE)
    public Flux<BranchInfoDto> getBranches(String username, String repoName) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{username}/{repoName}/branches").build(username, repoName))
                .retrieve()
                .bodyToFlux(BranchDto.class)
                .parallel(4)
                .runOn(Schedulers.parallel())
                .map(branch -> new BranchInfoDto(branch.name(), branch.commit().sha()))
                .sequential()
                .timeout(Duration.ofSeconds(5))
                .collectList()
                .flatMapMany(Flux::fromIterable)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error("WebClient error fetching branches: {}", ex.getMessage());
                    return Flux.empty();
                });
    }

    @Override
    public Flux<RepositoryInfoDto> getNonForkRepositories(String username) {

        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(RepositoryDto.class)
                .filter(repo -> !repo.fork())
                .flatMap(repo -> getBranches(username, repo.name())
                        .collectList()
                        .map(branches -> new RepositoryInfoDto(repo.name(), repo.owner().login(), branches))
                        .subscribeOn(Schedulers.parallel()))
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                        log.warn("Rate limit exceeded: {}", ex.getMessage());
                        return Flux.error(ex);
                    } else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        log.warn("User or resource not found: {}", ex.getMessage());
                        return Flux.error(ex);
                    }
                    return Flux.empty();
                })
                .doOnComplete(() -> log.info("Successfully fetched repositories for user: {}", username));
    }
}