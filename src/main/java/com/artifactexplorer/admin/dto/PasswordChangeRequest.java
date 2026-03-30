package com.artifactexplorer.admin.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

 // admin/dto/PasswordChangeRequest.java
public record PasswordChangeRequest(
    @NotBlank String currentPassword,
    @NotBlank @Size(min = 8) String newPassword
) {} 
