package com.artifactexplorer.admin.dto;

import jakarta.validation.constraints.NotBlank;

 // admin/dto/AuthRequest.java
public record AuthRequest(
    @NotBlank String username,
    @NotBlank String password
) {} 