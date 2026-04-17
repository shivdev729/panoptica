package com.artifactexplorer.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artifactexplorer.admin.entity.AdminActionLog;
import com.artifactexplorer.common.ActionType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// admin/repository/AdminActionLogRepository.java

@Repository
public interface AdminActionLogRepository extends JpaRepository<AdminActionLog, Long> {

    Page<AdminActionLog> findByEntityTypeAndEntityIdOrderByPerformedAtDesc(
            String entityType, String entityId, Pageable pageable);

    Page<AdminActionLog> findByAdmin_AdminIdOrderByPerformedAtDesc(
            String adminId, Pageable pageable);

    Page<AdminActionLog> findByActionAndEntityType(
            ActionType action, String entityType, Pageable pageable);

    Page<AdminActionLog> findAllByOrderByPerformedAtDesc(Pageable pageable);
}