package com.artifactexplorer.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.artifactexplorer.admin.dto.AdminRoleRequest;
import com.artifactexplorer.admin.dto.AdminRoleResponse;
import com.artifactexplorer.admin.dto.AdminUserRequest;
import com.artifactexplorer.admin.dto.AdminUserResponse;
import com.artifactexplorer.admin.dto.AuthRequest;
import com.artifactexplorer.admin.dto.AuthResponse;
import com.artifactexplorer.admin.dto.PasswordChangeRequest;
import com.artifactexplorer.admin.entity.AdminActionLog;
import com.artifactexplorer.admin.service.AdminUserService;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// admin/controller/AdminUserController.java
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService service;

    // ── Auth (public) ─────────────────────────────────────────

    @PostMapping("/api/admin/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity.ok(service.login(req));
    }

    // ── Users (ADMIN only) ────────────────────────────────────

    @GetMapping("/api/admin/users")
    public ResponseEntity<List<AdminUserResponse>> listUsers() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<AdminUserResponse> getUser(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/api/admin/users")
    public ResponseEntity<AdminUserResponse> createUser(
            @Valid @RequestBody AdminUserRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(req, (String) auth.getPrincipal()));
    }

    @PutMapping("/api/admin/users/{id}")
    public ResponseEntity<AdminUserResponse> updateUser(
            @PathVariable String id,
            @Valid @RequestBody AdminUserRequest req,
            Authentication auth) {
        return ResponseEntity.ok(service.update(id, req, (String) auth.getPrincipal()));
    }

    @PatchMapping("/api/admin/users/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String id,
            @Valid @RequestBody PasswordChangeRequest req,
            Authentication auth) {
        service.changePassword(id, req, (String) auth.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/admin/users/{id}/active")
    public ResponseEntity<AdminUserResponse> setActive(
            @PathVariable String id,
            @RequestParam boolean value,
            Authentication auth) {
        return ResponseEntity.ok(service.setActive(id, value, (String) auth.getPrincipal()));
    }

    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id,
            Authentication auth) {
        service.delete(id, (String) auth.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    // ── Roles ─────────────────────────────────────────────────

    @GetMapping("/api/admin/roles")
    public ResponseEntity<List<AdminRoleResponse>> listRoles() {
        return ResponseEntity.ok(service.findAllRoles());
    }

    @PostMapping("/api/admin/roles")
    public ResponseEntity<AdminRoleResponse> createRole(
            @Valid @RequestBody AdminRoleRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createRole(req, (String) auth.getPrincipal()));
    }

    @PutMapping("/api/admin/roles/{id}")
    public ResponseEntity<AdminRoleResponse> updateRole(
            @PathVariable String id,
            @Valid @RequestBody AdminRoleRequest req,
            Authentication auth) {
        return ResponseEntity.ok(service.updateRole(id, req, (String) auth.getPrincipal()));
    }

    @DeleteMapping("/api/admin/roles/{id}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable String id,
            Authentication auth) {
        service.deleteRole(id, (String) auth.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    // ── Action log ────────────────────────────────────────────

    @GetMapping("/api/admin/logs")
    public ResponseEntity<Page<AdminActionLog>> getLogs(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String entityId,
            @RequestParam(required = false) String adminId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(service.getLogs(
                entityType, entityId, adminId,
                PageRequest.of(page, size, Sort.by("performedAt").descending())));
    }
} 
