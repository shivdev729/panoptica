package com.artifactexplorer.museum.dto;

import jakarta.validation.constraints.NotBlank;

public record MuseumRequest(
    @NotBlank String museumId,
    @NotBlank String name,
    String location
) {} 
