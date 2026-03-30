package com.artifactexplorer.admin.dto;

import java.time.OffsetDateTime;

import com.artifactexplorer.admin.entity.AdminUser;


 // admin/dto/AdminUserResponse.java
public record AdminUserResponse(
    String adminId,
    String username,
    String email,
    String roleId,
    String roleName,
    boolean isActive,
    OffsetDateTime createdAt,
    OffsetDateTime lastLogin
) {
    public static AdminUserResponse from(AdminUser u) {
        return new AdminUserResponse(
            u.getAdminId(), u.getUsername(), u.getEmail(),
            u.getRole().getRoleId(), u.getRole().getName(),
            u.isActive(), u.getCreatedAt(), u.getLastLogin()
        );
    }
}