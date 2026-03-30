package com.artifactexplorer.museum.dto;

import java.time.OffsetDateTime;

import com.artifactexplorer.museum.entity.Museum;

// museum/dto/MuseumResponse.java
public record MuseumResponse(
    String museumId,
    String name,
    String location,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static MuseumResponse from(Museum m) {
        return new MuseumResponse(
            m.getMuseumId(), m.getName(), m.getLocation(),
            m.getAudit().getCreatedAt(), m.getAudit().getUpdatedAt()
        );
    }
}