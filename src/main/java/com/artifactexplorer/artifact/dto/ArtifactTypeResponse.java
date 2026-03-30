package com.artifactexplorer.artifact.dto;

import com.artifactexplorer.artifact.entity.ArtifactType;

// artifact/dto/ArtifactTypeResponse.java
public record ArtifactTypeResponse(
    String typeId, String name, String parentTypeId
) {
    public static ArtifactTypeResponse from(ArtifactType t) {
        return new ArtifactTypeResponse(
            t.getTypeId(), t.getName(),
            t.getParentType() != null ? t.getParentType().getTypeId() : null
        );
    }
}
