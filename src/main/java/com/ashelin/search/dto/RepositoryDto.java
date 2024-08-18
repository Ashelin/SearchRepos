package com.ashelin.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RepositoryDto(
        @JsonProperty("name") String name,
        @JsonProperty("owner") Owner owner,
        @JsonProperty("fork") boolean fork
) {

    public record Owner(@JsonProperty("login") String login) {}
}