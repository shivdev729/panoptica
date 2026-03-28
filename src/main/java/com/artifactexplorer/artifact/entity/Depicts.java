package com.artifactexplorer.artifact.entity;

import jakarta.persistence.*;

import com.artifactexplorer.deity.entity.Deity;

// artifact/entity/Depicts.java
@Entity
@Table(name = "depicts")
public class Depicts {

    @EmbeddedId
    private DepictsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artifactId")
    @JoinColumn(name = "artifact_id")
    private Artifact artifact;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("deityId")
    @JoinColumn(name = "deity_id")
    private Deity deity;

    @Column
    private String motif;

    // getters, setters
}

