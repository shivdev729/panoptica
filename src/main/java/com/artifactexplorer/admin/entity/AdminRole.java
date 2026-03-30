package com.artifactexplorer.admin.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

 // admin/entity/AdminRole.java
@Entity
@Table(name = "admin_role")
public class AdminRole {

    @Id
    @Column(name = "role_id", length = 20)
    private String roleId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, List<String>> permissions = new HashMap<>();

    // getters, setters
    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Map<String, List<String>> getPermissions() { return permissions; }
    public void setPermissions(Map<String, List<String>> permissions) { this.permissions = permissions; }
    
}