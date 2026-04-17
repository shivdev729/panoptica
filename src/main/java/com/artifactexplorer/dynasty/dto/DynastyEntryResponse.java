package com.artifactexplorer.dynasty.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.artifactexplorer.dynasty.entity.Dynasty;

public record DynastyEntryResponse(
    String dynastyId, String name,
    Integer periodStart, Integer periodEnd,
    OffsetDateTime createdAt, OffsetDateTime updatedAt
    ,List<String> ruledStates
) {
    public static DynastyResponse from(Dynasty d) {
        return new DynastyResponse(
            d.getDynastyId(), d.getName(),
            d.getPeriodStart(), d.getPeriodEnd(),
            d.getAudit().getCreatedAt(), d.getAudit().getUpdatedAt()
        );
    }
}