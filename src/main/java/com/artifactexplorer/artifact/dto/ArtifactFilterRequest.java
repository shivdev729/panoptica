package com.artifactexplorer.artifact.dto;

import com.artifactexplorer.common.ArtifactStatus;


// artifact/dto/ArtifactFilterRequest.java
// Used as @ModelAttribute for GET query params
public record ArtifactFilterRequest(
    String typeId,
    String dynastyId,
    String museumId,
    String regionId,       // filters via dynasty → ruled → region
    String material,
    ArtifactStatus status,
    String name,
    int page,
    int size
) {
    public ArtifactFilterRequest {
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 20;
    }
}