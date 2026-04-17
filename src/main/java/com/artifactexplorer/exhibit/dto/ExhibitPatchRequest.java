package com.artifactexplorer.exhibit.dto;

import java.time.LocalDate;
import java.util.Optional;

import com.artifactexplorer.common.ExhibitStatus;

public record ExhibitPatchRequest(
      Optional<String> title,
    Optional<String> description,
     Optional<String> museumId,
     Optional<LocalDate> startsOn,
    Optional<LocalDate> endsOn,
    Optional<Boolean> isRecurring,
    Optional<ExhibitStatus> status
) {
    // default constructor fills all as empty Optional
    // so missing JSON fields become Optional.empty(), not null
    public ExhibitPatchRequest {
        title        = title        != null ? title        : Optional.empty();
        description  = description  != null ? description  : Optional.empty();
        museumId     = museumId     != null ? museumId     : Optional.empty();
        startsOn     = startsOn     != null ? startsOn     : Optional.empty();
        endsOn       = endsOn       != null ? endsOn       : Optional.empty();
        isRecurring  = isRecurring  != null ? isRecurring  : Optional.empty();
        status       = status       != null ? status       : Optional.empty();
    }
} 