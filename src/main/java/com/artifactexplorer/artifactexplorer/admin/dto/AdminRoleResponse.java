package com.artifactexplorer.admin.dto;

import com.artifactexplorer.admin.entity.AdminRole;

import java.util.List;
import java.util.Map;

 // admin/dto/AdminRoleResponse.java
public record AdminRoleResponse(
    String roleId,
    String name,
    Map<String, List<String>> permissions
) {
    public static AdminRoleResponse from(AdminRole r) {
        return new AdminRoleResponse(r.getRoleId(), r.getName(), r.getPermissions());
    }
}