package com.artifactexplorer.artifact.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;


// artifact/entity/ArtifactType.java
@Entity
@Table(name = "artifact_type")
public class ArtifactType {

    @Id
    @Column(name = "type_id", length = 20)
    private String typeId;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_type_id")
    private ArtifactType parentType;

    @OneToMany(mappedBy = "parentType")
    private List<ArtifactType> children = new ArrayList<>();

    // getters, setters
}