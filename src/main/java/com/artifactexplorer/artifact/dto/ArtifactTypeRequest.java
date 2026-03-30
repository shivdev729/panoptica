package com.artifactexplorer.artifact.dto;

import jakarta.validation.constraints.NotBlank;

// artifact/dto/ArtifactTypeRequest.java
public record ArtifactTypeRequest(
    @NotBlank String typeId,
    @NotBlank String name,
    String parentTypeId
) {}