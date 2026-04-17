package com.artifactexplorer.deity.dto;

import com.artifactexplorer.deity.entity.Deity;
import java.time.OffsetDateTime;

import java.util.Set;

// deity/dto/DeityResponse.java
public record DeityResponse(
    String deityId, String name,
    Set<String> traditions,
    OffsetDateTime createdAt, OffsetDateTime updatedAt
) {
    public static DeityResponse from(Deity d) {
        return new DeityResponse(
            d.getDeityId(), d.getName(), d.getTraditions(),
            d.getAudit().getCreatedAt(), d.getAudit().getUpdatedAt()
        );
    }
}