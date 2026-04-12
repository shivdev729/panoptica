package com.artifactexplorer.artifact.dto;

import com.artifactexplorer.artifact.entity.Artifact;
import java.time.OffsetDateTime;

import com.artifactexplorer.common.ArtifactStatus;
import com.artifactexplorer.artifact.dto.ArtifactTypeResponse;
import com.artifactexplorer.artifact.entity.Depicts;

import java.util.Set;
import java.util.List;

// artifact/dto/ArtifactResponse.java
public record ArtifactResponse(
    String artifactId,
    String name,
    String description,
    ArtifactTypeResponse type,

    // dynasty with period
    String dynastyId,
    String dynastyName,
    Integer periodStart,
    Integer periodEnd,

    // museum
    String museumId,
    String museumName,

    // region — via dynasty → ruled
    List<RegionInfo> regions,

    ArtifactStatus status,
    Set<String> materials,

    // deity traditions — via depicts → deity → deity_tradition
    List<DeityInfo> depictedDeities,

    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    String createdBy,
    String updatedBy
) {
    // nested info records
    public record RegionInfo(
        String regionId,
        String name,
        Set<String> modernStates
    ) {}

    public record DeityInfo(
        String deityId,
        String name,
        Set<String> traditions,
        String motif
    ) {}

    public static ArtifactResponse from(Artifact a, List<Depicts> depictsList) {
        List<RegionInfo> regions = a.getDynasty() != null
            ? a.getDynasty().getRuledRegions().stream()
                .map(r -> new RegionInfo(
                    r.getRegionId(),
                    r.getName(),
                    r.getModernStates()
                )).toList()
            : List.of();

        List<DeityInfo> deities = depictsList.stream()
            .map(d -> new DeityInfo(
                d.getDeity().getDeityId(),
                d.getDeity().getName(),
                d.getDeity().getTraditions(),
                d.getMotif()
            )).toList();

        return new ArtifactResponse(
            a.getArtifactId(),
            a.getName(),
            a.getDescription(),
            a.getType() != null ? ArtifactTypeResponse.from(a.getType()) : null,
            a.getDynasty() != null ? a.getDynasty().getDynastyId()  : null,
            a.getDynasty() != null ? a.getDynasty().getName()        : null,
            a.getDynasty() != null ? a.getDynasty().getPeriodStart() : null,
            a.getDynasty() != null ? a.getDynasty().getPeriodEnd()   : null,
            a.getMuseum()  != null ? a.getMuseum().getMuseumId()     : null,
            a.getMuseum()  != null ? a.getMuseum().getName()         : null,
            regions,
            a.getStatus(),
            a.getMaterials(),
            deities,
            a.getAudit().getCreatedAt(),
            a.getAudit().getUpdatedAt(),
            a.getAudit().getCreatedBy(),
            a.getAudit().getUpdatedBy()
        );
    }
}