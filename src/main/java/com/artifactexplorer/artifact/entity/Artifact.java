package com.artifactexplorer.artifact.entity;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import com.artifactexplorer.dynasty.entity.Dynasty;
import com.artifactexplorer.museum.entity.Museum;
import com.artifactexplorer.deity.entity.Deity;
import com.artifactexplorer.common.ArtifactStatus;
import com.artifactexplorer.common.AuditFields;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.FetchType;

import jakarta.persistence.Embedded;


// artifact/entity/Artifact.java
@Entity
@Table(name = "artifact")
public class Artifact {

    @Id
    @Column(name = "artifact_id", length = 20)
    private String artifactId;

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ArtifactType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dynasty_id")
    private Dynasty dynasty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "museum_id")
    private Museum museum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtifactStatus status = ArtifactStatus.DRAFT;

    @ElementCollection
    @CollectionTable(
        name = "artifact_material",
        joinColumns = @JoinColumn(name = "artifact_id")
    )
    @Column(name = "material")
    private Set<String> materials = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "depicts",
        joinColumns = @JoinColumn(name = "artifact_id"),
        inverseJoinColumns = @JoinColumn(name = "deity_id")
    )
    private Set<Deity> depictedDeities = new HashSet<>();

    @Embedded
    private AuditFields audit = new AuditFields();

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        audit.setCreatedAt(now);
        audit.setUpdatedAt(now);
    }

    @PreUpdate
    void onUpdate() {
        audit.setUpdatedAt(OffsetDateTime.now());
    }
    // getters, setters
    public String getArtifactId() {
        return artifactId;
    }
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public ArtifactType getType() {
        return type;
    }
    public void setType(ArtifactType type) {
        this.type = type;
    }
    public Dynasty getDynasty() {
        return dynasty;
    }
    public void setDynasty(Dynasty dynasty) {
        this.dynasty = dynasty;
    }
    public Museum getMuseum() {
        return museum;
    }
    public void setMuseum(Museum museum) {
        this.museum = museum;
    }
    public ArtifactStatus getStatus() {
        return status;
    }
    public void setStatus(ArtifactStatus status) {
        this.status = status;
    }
    public Set<String> getMaterials() {
        return materials;
    }
    public void setMaterials(Set<String> materials) {
        this.materials = materials;
    }
    public Set<Deity> getDepictedDeities() {
        return depictedDeities;
    }
    public void setDepictedDeities(Set<Deity> depictedDeities) {
        this.depictedDeities = depictedDeities;
    }
    public AuditFields getAudit() {
        return audit;
    }
}