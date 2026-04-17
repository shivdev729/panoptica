package com.artifactexplorer.exhibit.dto;

import java.time.OffsetDateTime;

import com.artifactexplorer.exhibit.entity.ExhibitArtifact;

// exhibit/dto/ExhibitArtifactResponse.java
public record ExhibitArtifactResponse(
    String exhibitId,
    String artifactId,
    String artifactName,
    Integer displayOrder,
    String notes,
    String addedBy,
    OffsetDateTime addedAt
) {
    public static ExhibitArtifactResponse from(ExhibitArtifact ea) {
        return new ExhibitArtifactResponse(
            ea.getExhibit().getExhibitId(),
            ea.getArtifact().getArtifactId(),
            ea.getArtifact().getName(),
            ea.getDisplayOrder(), ea.getNotes(),
            ea.getAddedBy() != null ? ea.getAddedBy().getAdminId() : null,
            ea.getAddedAt()
        );
    }
}
