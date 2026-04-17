package com.artifactexplorer.exhibit.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import com.artifactexplorer.museum.entity.Museum;
import com.artifactexplorer.admin.entity.AdminUser;
import com.artifactexplorer.common.ExhibitStatus;

// exhibit/entity/Exhibit.java
@Entity
@Table(name = "exhibit")
public class Exhibit {

    @Id
    @Column(name = "exhibit_id", length = 20)
    private String exhibitId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "museum_id", nullable = false)
    private Museum museum;

    @Column(name = "starts_on", nullable = false)
    private LocalDate startsOn;

    @Column(name = "ends_on")
    private LocalDate endsOn;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExhibitStatus status = ExhibitStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private AdminUser createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private AdminUser updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "exhibit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExhibitArtifact> exhibitArtifacts = new ArrayList<>();

    @PrePersist
    void onCreate() { createdAt = updatedAt = OffsetDateTime.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = OffsetDateTime.now(); }

    // getters, setters
    public String getExhibitId() { return exhibitId; }
    public void setExhibitId(String exhibitId) { this.exhibitId = exhibitId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Museum getMuseum() { return museum; }
    public void setMuseum(Museum museum) { this.museum = museum; }
    public LocalDate getStartsOn() { return startsOn; }
    public void setStartsOn(LocalDate startsOn) { this.startsOn = startsOn; }
    public LocalDate getEndsOn() { return endsOn; }
    public void setEndsOn(LocalDate endsOn) { this.endsOn = endsOn;}
    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }
    public ExhibitStatus getStatus() { return status; }
    public void setStatus(ExhibitStatus status) { this.status = status; }
    public AdminUser getCreatedBy() { return createdBy; }
    public void setCreatedBy(AdminUser createdBy) { this.createdBy = createdBy;}
    public AdminUser getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(AdminUser updatedBy) { this.updatedBy = updatedBy;}
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<ExhibitArtifact> getExhibitArtifacts() { return exhibitArtifacts; }
    public void setExhibitArtifacts(List<ExhibitArtifact> exhibitArtifacts) { this.exhibitArtifacts = exhibitArtifacts; }
}