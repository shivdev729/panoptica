package com.artifactexplorer.artifact.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

// artifact/entity/ArtifactRevision.java
@Entity
@Table(name = "artifact_revision")
public class ArtifactRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revision_id")
    private Integer revisionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artifact_id", nullable = false)
    private Artifact artifact;

    @Column(name = "revised_at", nullable = false)
    private OffsetDateTime revisedAt;

    @Column(name = "revised_by")
    private String revisedBy;

    @Column(name = "change_note")
    private String changeNote;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> snapshot;

    @PrePersist
    void onCreate() { revisedAt = OffsetDateTime.now(); }

    // getters, setters
    public Integer getRevisionId() { return revisionId; }
    public Artifact getArtifact() { return artifact; }
    public void setArtifact(Artifact artifact) { this.artifact = artifact; }
    public OffsetDateTime getRevisedAt() { return revisedAt; }
    public String getRevisedBy() { return revisedBy; }
    public void setRevisedBy(String revisedBy) { this.revisedBy = revisedBy; }
    public String getChangeNote() { return changeNote; }
    public void setChangeNote(String changeNote) { this.changeNote = changeNote; }
    public Map<String, Object> getSnapshot() { return snapshot; }
    public void setSnapshot(Map<String, Object> snapshot) { this.snapshot = snapshot; }

}