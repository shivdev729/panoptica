package com.artifactexplorer.exhibit.dto;

import jakarta.validation.constraints.NotBlank;
 // exhibit/dto/ExhibitArtifactRequest.java
public record ExhibitArtifactRequest(
    @NotBlank String artifactId,
    Integer displayOrder,
    String notes
) {} 
