package com.ashelin.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BranchDto(
        @JsonProperty("name") String name,
        @JsonProperty("commit") Commit commit
) {

    public record Commit(@JsonProperty("sha") String sha) {}
}