package com.artifactexplorer.museum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;

import com.artifactexplorer.common.AuditFields;
// museum/entity/Museum.java
@Entity
@Table(name = "museum")
public class Museum {

    @Id
    @Column(name = "museum_id", length = 20)
    private String museumId;

    @Column(nullable = false)
    private String name;

    @Column
    private String location;

    @Embedded
    private AuditFields audit = new AuditFields();

    // getters, setters
}

