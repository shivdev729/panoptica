package com.artifactexplorer.admin.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.artifactexplorer.admin.dto.AdminRoleRequest;
import com.artifactexplorer.admin.dto.AdminRoleResponse;
import com.artifactexplorer.admin.dto.AdminUserRequest;
import com.artifactexplorer.admin.dto.AdminUserResponse;
import com.artifactexplorer.admin.dto.AuthRequest;
import com.artifactexplorer.admin.dto.AuthResponse;
import com.artifactexplorer.admin.dto.PasswordChangeRequest;
import com.artifactexplorer.admin.entity.AdminActionLog;
import com.artifactexplorer.admin.entity.AdminRole;
import com.artifactexplorer.admin.entity.AdminUser;
import com.artifactexplorer.admin.repository.AdminActionLogRepository;
import com.artifactexplorer.admin.repository.AdminRoleRepository;
import com.artifactexplorer.admin.repository.AdminUserRepository;
import com.artifactexplorer.common.ActionType;
import com.artifactexplorer.common.security.JwtUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

 // admin/service/AdminUserService.java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {

    private final AdminUserRepository    userRepo;
    private final AdminRoleRepository    roleRepo;
    private final AdminActionLogRepository logRepo;
    private final JwtUtil                jwtUtil;
    private final PasswordEncoder        passwordEncoder;

    // ── Auth ──────────────────────────────────────────────────

    @Transactional
    public AuthResponse login(AuthRequest req) {
        AdminUser user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> new EntityNotFoundException("Invalid credentials"));

        if (!user.isActive())
            throw new IllegalStateException("Account is disabled");

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash()))
            throw new IllegalArgumentException("Invalid credentials");

        user.setLastLogin(OffsetDateTime.now());
        userRepo.save(user);

        String token = jwtUtil.generate(user.getAdminId(), user.getRole().getName());
        Instant expiresAt = Instant.now().plusMillis(jwtUtil.getExpirationMs());

        log(user, ActionType.CREATE, "admin_user", user.getAdminId(), null, "login");

        return new AuthResponse(token, user.getAdminId(),
                user.getUsername(), user.getRole().getName(), expiresAt);
    }

    // ── Users ─────────────────────────────────────────────────

    public List<AdminUserResponse> findAll() {
        return userRepo.findAll().stream().map(AdminUserResponse::from).toList();
    }

    public AdminUserResponse findById(String id) {
        return userRepo.findById(id)
                .map(AdminUserResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Admin user not found: " + id));
    }

    @Transactional
    public AdminUserResponse create(AdminUserRequest req, String performedBy) {
        if (userRepo.existsById(req.adminId()))
            throw new IllegalArgumentException("Admin ID already exists: " + req.adminId());
        if (userRepo.findByUsername(req.username()).isPresent())
            throw new IllegalArgumentException("Username already taken: " + req.username());
        if (userRepo.findByEmail(req.email()).isPresent())
            throw new IllegalArgumentException("Email already registered: " + req.email());

        AdminRole role = roleRepo.findById(req.roleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + req.roleId()));

        AdminUser user = new AdminUser();
        user.setAdminId(req.adminId());
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setRole(role);
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setActive(true);

        AdminUser saved = userRepo.save(user);
        log(performedBy, ActionType.CREATE, "admin_user", saved.getAdminId(),
                null, "created admin user");
        return AdminUserResponse.from(saved);
    }

    @Transactional
    public AdminUserResponse update(String id, AdminUserRequest req, String performedBy) {
        AdminUser user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin user not found: " + id));

        Map<String, Object> before = snapshot(user);

        AdminRole role = roleRepo.findById(req.roleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + req.roleId()));

        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setRole(role);

        AdminUser saved = userRepo.save(user);
        log(performedBy, ActionType.UPDATE, "admin_user", id,
                Map.of("before", before, "after", snapshot(saved)), null);
        return AdminUserResponse.from(saved);
    }

    @Transactional
    public void changePassword(String id, PasswordChangeRequest req, String performedBy) {
        AdminUser user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin user not found: " + id));

        if (!passwordEncoder.matches(req.currentPassword(), user.getPasswordHash()))
            throw new IllegalArgumentException("Current password is incorrect");

        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        userRepo.save(user);
        log(performedBy, ActionType.UPDATE, "admin_user", id, null, "password changed");
    }

    @Transactional
    public AdminUserResponse setActive(String id, boolean active, String performedBy) {
        AdminUser user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin user not found: " + id));
        user.setActive(active);
        AdminUser saved = userRepo.save(user);
        log(performedBy, ActionType.UPDATE, "admin_user", id, null,
                active ? "account enabled" : "account disabled");
        return AdminUserResponse.from(saved);
    }

    @Transactional
    public void delete(String id, String performedBy) {
        if (!userRepo.existsById(id))
            throw new EntityNotFoundException("Admin user not found: " + id);
        userRepo.deleteById(id);
        log(performedBy, ActionType.DELETE, "admin_user", id, null, "deleted");
    }

    // ── Roles ─────────────────────────────────────────────────

    public List<AdminRoleResponse> findAllRoles() {
        return roleRepo.findAll().stream().map(AdminRoleResponse::from).toList();
    }

    @Transactional
    public AdminRoleResponse createRole(AdminRoleRequest req, String performedBy) {
        if (roleRepo.existsById(req.roleId()))
            throw new IllegalArgumentException("Role ID already exists: " + req.roleId());
        AdminRole role = new AdminRole();
        role.setRoleId(req.roleId());
        role.setName(req.name());
        role.setPermissions(req.permissions() != null ? req.permissions() : new HashMap<>());
        AdminRole saved = roleRepo.save(role);
        log(performedBy, ActionType.CREATE, "admin_role", saved.getRoleId(), null, "created role");
        return AdminRoleResponse.from(saved);
    }

    @Transactional
    public AdminRoleResponse updateRole(String id, AdminRoleRequest req, String performedBy) {
        AdminRole role = roleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + id));
        role.setName(req.name());
        if (req.permissions() != null) role.setPermissions(req.permissions());
        AdminRole saved = roleRepo.save(role);
        log(performedBy, ActionType.UPDATE, "admin_role", id, null, "updated role");
        return AdminRoleResponse.from(saved);
    }

    @Transactional
    public void deleteRole(String id, String performedBy) {
        if (!roleRepo.existsById(id))
            throw new EntityNotFoundException("Role not found: " + id);
        roleRepo.deleteById(id);
        log(performedBy, ActionType.DELETE, "admin_role", id, null, "deleted role");
    }

    // ── Action log query ──────────────────────────────────────

    public Page<AdminActionLog> getLogs(String entityType, String entityId,
                                        String adminId, Pageable pageable) {
        if (entityType != null && entityId != null)
            return logRepo.findByEntityTypeAndEntityIdOrderByPerformedAtDesc(
                    entityType, entityId, pageable);
        if (adminId != null)
            return logRepo.findByAdmin_AdminIdOrderByPerformedAtDesc(adminId, pageable);
        return logRepo.findAllByOrderByPerformedAtDesc(pageable);
    }

    // ── Helpers ───────────────────────────────────────────────

    private void log(AdminUser actor, ActionType action,
                     String entityType, String entityId,
                     Map<String, Object> diff, String note) {
        log(actor.getAdminId(), action, entityType, entityId, diff, note);
    }

    private void log(String adminId, ActionType action,
                     String entityType, String entityId,
                     Map<String, Object> diff, String note) {
        AdminActionLog entry = new AdminActionLog();
        userRepo.findById(adminId).ifPresent(entry::setAdmin);
        entry.setAction(action);
        entry.setEntityType(entityType);
        entry.setEntityId(entityId);
        entry.setDiff(diff);
        entry.setNote(note);
        logRepo.save(entry);
    }

    private Map<String, Object> snapshot(AdminUser u) {
        return Map.of(
            "username", u.getUsername(),
            "email",    u.getEmail(),
            "roleId",   u.getRole().getRoleId(),
            "isActive", u.isActive()
        );
    }
} 
