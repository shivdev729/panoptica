package com.artifactexplorer.exhibit.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.artifactexplorer.admin.entity.AdminUser;


// exhibit/entity/ExhibitRevision.java
@Entity
@Table(name = "exhibit_revision")
public class ExhibitRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revision_id")
    private Integer revisionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exhibit_id", nullable = false)
    private Exhibit exhibit;

    @Column(name = "revised_at", nullable = false)
    private OffsetDateTime revisedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revised_by")
    private AdminUser revisedBy;

    @Column(name = "change_note")
    private String changeNote;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> snapshot;

    @PrePersist
    void onCreate() { revisedAt = OffsetDateTime.now(); }

    // getters, setters
    public Integer getRevisionId() { return revisionId; }
    public void setRevisionId(Integer revisionId) { this.revisionId = revisionId; }
    public Exhibit getExhibit() { return exhibit; }
    public void setExhibit(Exhibit exhibit) { this.exhibit = exhibit; }
    public OffsetDateTime getRevisedAt() { return revisedAt; }
    public void setRevisedAt(OffsetDateTime revisedAt) { this.revisedAt = revisedAt; }
    public AdminUser getRevisedBy() { return revisedBy; }
    public void setRevisedBy(AdminUser revisedBy) { this.revisedBy = revisedBy; }
    public String getChangeNote() { return changeNote; }
    public void setChangeNote(String changeNote) { this.changeNote = changeNote; }
    public Map<String, Object> getSnapshot() { return snapshot; }
    public void setSnapshot(Map<String, Object> snapshot) { this.snapshot = snapshot; }
    
}