package com.ashelin.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BranchInfoDto(
        @JsonProperty("name") String name,
        @JsonProperty("commitSha") String commitSha
) {}



