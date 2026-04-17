package com.artifactexplorer.exhibit.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.artifactexplorer.common.ExhibitStatus;
import com.artifactexplorer.exhibit.entity.Exhibit;

 // exhibit/dto/ExhibitResponse.java
public record ExhibitResponse(
    String exhibitId,
    String title,
    String description,
    String museumId,
    String museumName,
    LocalDate startsOn,
    LocalDate endsOn,
    boolean isRecurring,
    ExhibitStatus status,
    String createdBy,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static ExhibitResponse from(Exhibit e) {
        return new ExhibitResponse(
            e.getExhibitId(), e.getTitle(), e.getDescription(),
            e.getMuseum().getMuseumId(), e.getMuseum().getName(),
            e.getStartsOn(), e.getEndsOn(), e.isRecurring(), e.getStatus(),
            e.getCreatedBy() != null ? e.getCreatedBy().getAdminId() : null,
            e.getCreatedAt(), e.getUpdatedAt()
        );
    }
} 
