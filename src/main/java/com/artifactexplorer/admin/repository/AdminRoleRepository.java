package com.artifactexplorer.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artifactexplorer.admin.entity.AdminRole;


import java.util.Optional;
// admin/repository/AdminRoleRepository.java
@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, String> {
    Optional<AdminRole> findByName(String name);
}

