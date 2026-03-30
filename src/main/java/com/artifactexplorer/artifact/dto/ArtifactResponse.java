package com.artifactexplorer.artifact.dto;

import com.artifactexplorer.artifact.entity.Artifact;
import java.time.OffsetDateTime;

import com.artifactexplorer.common.ArtifactStatus;
import com.artifactexplorer.artifact.dto.ArtifactTypeResponse;

import java.util.Set;

// artifact/dto/ArtifactResponse.java

public record ArtifactResponse(
    String artifactId,
    String name,
    String description,
    ArtifactTypeResponse type,
    String dynastyId,
    String dynastyName,
    String museumId,
    String museumName,
    ArtifactStatus status,
    Set<String> materials,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static ArtifactResponse from(Artifact a) {
        return new ArtifactResponse(
            a.getArtifactId(), a.getName(), a.getDescription(),
            a.getType() != null ? ArtifactTypeResponse.from(a.getType()) : null,
            a.getDynasty() != null ? a.getDynasty().getDynastyId() : null,
            a.getDynasty() != null ? a.getDynasty().getName() : null,
            a.getMuseum()  != null ? a.getMuseum().getMuseumId()  : null,
            a.getMuseum()  != null ? a.getMuseum().getName()  : null,
            a.getStatus(), a.getMaterials(),
            a.getAudit().getCreatedAt(), a.getAudit().getUpdatedAt()
        );
    }
}