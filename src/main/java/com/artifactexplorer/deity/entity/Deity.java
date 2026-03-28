package com.artifactexplorer.deity.entity;

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
// deity/entity/Deity.java
@Entity
@Table(name = "deity")
public class Deity {

    @Id
    @Column(name = "deity_id", length = 20)
    private String deityId;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(
        name = "deity_tradition",
        joinColumns = @JoinColumn(name = "deity_id")
    )
    @Column(name = "tradition")
    private Set<String> traditions = new HashSet<>();

    @Embedded
    private AuditFields audit = new AuditFields();

    // getters, setters
}

