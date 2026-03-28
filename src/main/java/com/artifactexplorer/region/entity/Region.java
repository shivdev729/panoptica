package com.artifactexplorer.region.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;


import java.util.Set;
import java.util.HashSet;

import com.artifactexplorer.common.AuditFields;
// region/entity/Region.java
@Entity
@Table(name = "region")
public class Region {

    @Id
    @Column(name = "region_id", length = 20)
    private String regionId;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(
        name = "region_modern_states",
        joinColumns = @JoinColumn(name = "region_id")
    )
    @Column(name = "modern_state")
    private Set<String> modernStates = new HashSet<>();

    @Embedded
    private AuditFields audit = new AuditFields();

    // getters, setters
}