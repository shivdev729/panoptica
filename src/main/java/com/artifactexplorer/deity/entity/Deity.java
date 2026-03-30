package com.artifactexplorer.deity.entity;

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
    public String getDeityId() {
        return deityId;
    }
    public void setDeityId(String deityId) {
        this.deityId = deityId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<String> getTraditions() {
        return traditions;
    }
    public void setTraditions(Set<String> traditions) {
        this.traditions = traditions;
    }
    public AuditFields getAudit() {
        return audit;
    }
    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
    
}

