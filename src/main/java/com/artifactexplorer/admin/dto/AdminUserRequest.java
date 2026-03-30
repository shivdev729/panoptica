package com.artifactexplorer.admin.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

 // admin/dto/AdminUserRequest.java
public record AdminUserRequest(
    @NotBlank String adminId,
    @NotBlank String username,
    @NotBlank @Email String email,
    @NotBlank String roleId,
    @NotBlank @Size(min = 8) String password
) {} 