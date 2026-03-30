package com.artifactexplorer.artifact.dto;

import jakarta.validation.constraints.NotBlank;
import com.artifactexplorer.common.ArtifactStatus;

import java.util.Set;


// artifact/dto/ArtifactRequest.java
public record ArtifactRequest(
    @NotBlank String artifactId,
    String name,
    String description,
    String typeId,
    String dynastyId,
    String museumId,
    ArtifactStatus status,
    Set<String> materials
) {}