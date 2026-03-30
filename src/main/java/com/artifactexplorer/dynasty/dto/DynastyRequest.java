package com.artifactexplorer.dynasty.dto;

import jakarta.validation.constraints.NotBlank;

// dynasty/dto/DynastyRequest.java
public record DynastyRequest(
    @NotBlank String dynastyId,
    @NotBlank String name,
    Integer periodStart,
    Integer periodEnd
) {}
