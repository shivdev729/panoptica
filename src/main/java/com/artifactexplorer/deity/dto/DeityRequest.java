package com.artifactexplorer.deity.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;

// deity/dto/DeityRequest.java
public record DeityRequest(
    @NotBlank String deityId,
    @NotBlank String name,
    Set<String> traditions
) {}
