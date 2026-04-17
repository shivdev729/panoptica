package com.artifactexplorer.region.dto;

import java.time.OffsetDateTime;
import java.util.Set;
import com.artifactexplorer.region.entity.Region;


public record RegionResponse(
    String regionId, String name,
    Set<String> modernStates,
    OffsetDateTime createdAt, OffsetDateTime updatedAt
) {
    public static RegionResponse from(Region r) {
        return new RegionResponse(
            r.getRegionId(), r.getName(), r.getModernStates(),
            r.getAudit().getCreatedAt(), r.getAudit().getUpdatedAt()
        );
    }
}
