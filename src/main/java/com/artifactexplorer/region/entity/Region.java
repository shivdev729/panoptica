package com.artifactexplorer.region.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.util.Set;
import java.time.OffsetDateTime;
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
    public String getRegionId() {
        return regionId;
    }

    public String getName() {
        return name;
    }

    public Set<String> getModernStates() {
        return modernStates;
    }

    public AuditFields getAudit() {
        return audit;
    }
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setModernStates(Set<String> modernStates) {
        this.modernStates = modernStates;
    }
    
}