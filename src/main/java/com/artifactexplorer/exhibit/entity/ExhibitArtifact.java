package com.artifactexplorer.exhibit.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

import com.artifactexplorer.artifact.entity.Artifact;
import com.artifactexplorer.admin.entity.AdminUser;

// exhibit/entity/ExhibitArtifact.java
@Entity
@Table(name = "exhibit_artifact")
public class ExhibitArtifact {

    @EmbeddedId
    private ExhibitArtifactId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("exhibitId")
    @JoinColumn(name = "exhibit_id")
    private Exhibit exhibit;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artifactId")
    @JoinColumn(name = "artifact_id")
    private Artifact artifact;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private AdminUser addedBy;

    @Column(name = "added_at", nullable = false)
    private OffsetDateTime addedAt;

    @PrePersist
    void onCreate() { addedAt = OffsetDateTime.now(); }

    // getters, setters
    public ExhibitArtifactId getId() { return id; }
    public void setId(ExhibitArtifactId id) { this.id = id; }
    public Exhibit getExhibit() { return exhibit; }
    public void setExhibit(Exhibit exhibit) { this.exhibit = exhibit; }
    public Artifact getArtifact() { return artifact; }
    public void setArtifact(Artifact artifact) { this.artifact = artifact; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public AdminUser getAddedBy() { return addedBy; }
    public void setAddedBy(AdminUser addedBy) { this.addedBy = addedBy; }
    public OffsetDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(OffsetDateTime addedAt) { this.addedAt = addedAt; }
    
}

