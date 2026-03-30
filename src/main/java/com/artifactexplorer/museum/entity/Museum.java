package com.artifactexplorer.museum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Version;

import java.time.OffsetDateTime;

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

    @Version private Long version;
    // getters, setters
    public String getMuseumId() { return museumId; }
    public void setMuseumId(String museumId) { this.museumId = museumId;}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public AuditFields getAudit() { return audit; }

}

