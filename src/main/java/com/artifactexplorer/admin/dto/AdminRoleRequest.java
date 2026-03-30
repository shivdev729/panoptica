package com.artifactexplorer.admin.dto;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;

 // admin/dto/AdminRoleRequest.java
public record AdminRoleRequest(
    @NotBlank String roleId,
    @NotBlank String name,
    Map<String, List<String>> permissions
) {} 
