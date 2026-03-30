package com.artifactexplorer.region.dto;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;


// region/dto/RegionRequest.java
public record RegionRequest(
    @NotBlank String regionId,
    @NotBlank String name,
    Set<String> modernStates
) {}
