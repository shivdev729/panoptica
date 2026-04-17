package com.artifactexplorer.exhibit.dto;

import java.time.LocalDate;

import com.artifactexplorer.common.ExhibitStatus;

 // exhibit/dto/ExhibitFilterRequest.java
public record ExhibitFilterRequest(
    String museumId,
    ExhibitStatus status,
    LocalDate from,
    LocalDate to,
    int page,
    int size
) {
    public ExhibitFilterRequest {
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 20;
    }
} 
