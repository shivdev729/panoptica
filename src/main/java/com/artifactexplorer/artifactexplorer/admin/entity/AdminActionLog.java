package com.artifactexplorer.admin.entity;

import java.time.OffsetDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.artifactexplorer.common.ActionType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


// admin/entity/AdminActionLog.java
@Entity
@Table(name = "admin_action_log")
public class AdminActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private AdminUser admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType action;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> diff;

    @Column(name = "performed_at", nullable = false)
    private OffsetDateTime performedAt;

    @Column(name = "ip_address", columnDefinition = "inet")
    private String ipAddress;

    @Column
    private String note;

    @PrePersist
    void onCreate() { performedAt = OffsetDateTime.now(); }

    // getters, setters
    public Long getLogId() { return logId; }
    public AdminUser getAdmin() { return admin; }
    public ActionType getAction() { return action; }
    public String getEntityType() { return entityType; }
    public String getEntityId() { return entityId; }
    public Map<String, Object> getDiff() { return diff; }
    public OffsetDateTime getPerformedAt() { return performedAt; }
    public String getIpAddress() { return ipAddress; }
    public String getNote() { return note; }
    public void setAdmin(AdminUser admin) { this.admin = admin; }
    public void setAction(ActionType action) { this.action = action; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public void setDiff(Map<String, Object> diff) { this.diff = diff; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setNote(String note) { this.note = note; }
} 