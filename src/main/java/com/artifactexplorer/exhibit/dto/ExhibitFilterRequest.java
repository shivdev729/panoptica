package com.artifactexplorer.exhibit.dto;

import java.time.LocalDate;

import com.artifactexplorer.common.ExhibitStatus;

 // exhibit/dto/ExhibitFilterRequest.java
public record ExhibitFilterRequest(
    String museumId,
    String museumName,  
     String regionName,    
    String title,
    ExhibitStatus status,
    LocalDate from,
    LocalDate to,
    Boolean isRecurring,
    Integer page,
    Integer size
) {
    public ExhibitFilterRequest {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0 || size > 100) size = 20;
    }
} 
