package com.artifactexplorer.artifact.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import com.artifactexplorer.dynasty.entity.Dynasty;
import com.artifactexplorer.museum.entity.Museum;
import com.artifactexplorer.deity.entity.Deity;
import com.artifactexplorer.common.ArtifactStatus;
import com.artifactexplorer.common.AuditFields;


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

    @ManyToMany
    @JoinTable(
        name = "ruled",
        joinColumns = @JoinColumn(name = "dynasty_id"),   // through dynasty
        inverseJoinColumns = @JoinColumn(name = "region_id")
    )
    // Note: ruled is on dynasty, not artifact — see Dynasty entity below

    @Embedded
    private AuditFields audit = new AuditFields();

    // getters, setters
}
