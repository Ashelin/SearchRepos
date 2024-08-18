package com.ashelin.search.service;

import com.ashelin.search.dto.BranchInfoDto;
import com.ashelin.search.dto.RepositoryInfoDto;
import reactor.core.publisher.Flux;

public interface SearchReposService {

    Flux<BranchInfoDto> getBranches(String username, String repoName);

    Flux<RepositoryInfoDto> getNonForkRepositories(String username);
}