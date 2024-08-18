package com.ashelin.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record RepositoryInfoDto(
        @JsonProperty("name") String name,
        @JsonProperty("ownerLogin") String ownerLogin,
        @JsonProperty("branches") List<BranchInfoDto> branches
) {}