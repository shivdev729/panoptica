package com.artifactexplorer.artifact.dto;

import com.artifactexplorer.common.ArtifactStatus;
import java.util.Optional;
import java.util.Set;

 // artifact/dto/ArtifactPatchRequest.java
public record ArtifactPatchRequest(
    Optional<String> name,
    Optional<String> description,
    Optional<String> typeId,
    Optional<String> dynastyId,
    Optional<String> museumId,
    Optional<ArtifactStatus> status,
    Optional<Set<String>> materials
) {
    // default constructor fills all as empty Optional
    // so missing JSON fields become Optional.empty(), not null
    public ArtifactPatchRequest {
        name        = name        != null ? name        : Optional.empty();
        description = description != null ? description : Optional.empty();
        typeId      = typeId      != null ? typeId      : Optional.empty();
        dynastyId   = dynastyId   != null ? dynastyId   : Optional.empty();
        museumId    = museumId    != null ? museumId    : Optional.empty();
        status      = status      != null ? status      : Optional.empty();
        materials   = materials   != null ? materials   : Optional.empty();
    }
} 