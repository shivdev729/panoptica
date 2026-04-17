package com.artifactexplorer.artifact.dto;

import com.artifactexplorer.common.ArtifactStatus;


// artifact/dto/ArtifactFilterRequest.java
// Used as @ModelAttribute for GET query params
public record ArtifactFilterRequest(
    String typeId,
    String dynastyId,
    String dynastyName,
    String museumId,
    String museumName,
    String material,
    ArtifactStatus status,
    String name,
    String regionId,       // filters via dynasty → ruled → region
    String regionName,
    Integer page,
    Integer size
) {
    public ArtifactFilterRequest {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 10 : size;
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 20;
    }
}