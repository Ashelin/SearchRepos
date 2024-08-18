package com.ashelin.search.controllers;

import com.ashelin.search.dto.BranchInfoDto;
import com.ashelin.search.dto.RepositoryInfoDto;
import com.ashelin.search.service.SearchReposService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchReposController {

    private final SearchReposService githubService;

    @GetMapping(value = "/users/{username}/repos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RepositoryInfoDto> getRepositories(@PathVariable String username) {
        return githubService.getNonForkRepositories(username);
    }

    @GetMapping("/users/{username}/repos/{repoName}/branches")
    public Flux<BranchInfoDto> getBranches(@PathVariable String username, @PathVariable String repoName) {
        return githubService.getBranches(username, repoName);
    }
}