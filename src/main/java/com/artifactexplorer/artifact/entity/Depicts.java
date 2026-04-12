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
    public DepictsId getId() {
        return id;
    }
    public void setId(DepictsId id) {
        this.id = id;
    }
    public Artifact getArtifact() {
        return artifact;
    }
    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }
    public Deity getDeity() {
        return deity;
    }
    public void setDeity(Deity deity) {
        this.deity = deity;
    }
    public String getMotif() {
        return motif;
    }
    public void setMotif(String motif) {
        this.motif = motif;
    }
}

