package com.artifactexplorer.dynasty.dto;

import java.time.OffsetDateTime;
import com.artifactexplorer.dynasty.entity.Dynasty;

// dynasty/dto/DynastyResponse.java
public record DynastyResponse(
    String dynastyId, String name,
    Integer periodStart, Integer periodEnd,
    OffsetDateTime createdAt, OffsetDateTime updatedAt
) {
    public static DynastyResponse from(Dynasty d) {
        return new DynastyResponse(
            d.getDynastyId(), d.getName(),
            d.getPeriodStart(), d.getPeriodEnd(),
            d.getAudit().getCreatedAt(), d.getAudit().getUpdatedAt()
        );
    }
}
