package com.artifactexplorer.exhibit.dto;

import java.time.LocalDate;

import com.artifactexplorer.common.ExhibitStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// exhibit/dto/ExhibitRequest.java
public record ExhibitRequest(
    @NotBlank String title,
    String description,
    @NotBlank String museumId,
    @NotNull LocalDate startsOn,
    LocalDate endsOn,
    boolean isRecurring,
    ExhibitStatus status
) {}
