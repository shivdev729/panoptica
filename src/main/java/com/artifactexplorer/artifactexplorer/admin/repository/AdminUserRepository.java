package com.artifactexplorer.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artifactexplorer.admin.entity.AdminUser;

import java.util.Optional;
import java.util.List;

// admin/repository/AdminUserRepository.java
@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, String> {
    Optional<AdminUser> findByUsername(String username);
    Optional<AdminUser> findByEmail(String email);
    List<AdminUser> findByIsActiveTrue();
    List<AdminUser> findByRole_RoleId(String roleId);
} 
