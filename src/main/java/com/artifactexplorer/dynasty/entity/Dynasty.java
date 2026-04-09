package com.artifactexplorer.dynasty.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import com.artifactexplorer.common.AuditFields;
import com.artifactexplorer.region.entity.Region;

// dynasty/entity/Dynasty.java
@Entity
@Table(name = "dynasty")
public class Dynasty {

    @Id
    @Column(name = "dynasty_id", length = 20)
    private String dynastyId;

    @Column(nullable = false)
    private String name;

    @Column(name = "period_start")
    private Integer periodStart;

    @Column(name = "period_end")
    private Integer periodEnd;

    @ManyToMany
    @JoinTable(
        name = "ruled",
        joinColumns = @JoinColumn(name = "dynasty_id"),
        inverseJoinColumns = @JoinColumn(name = "region_id")
    )
    private Set<Region> ruledRegions = new HashSet<>();

    @Embedded
    private AuditFields audit = new AuditFields();

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        audit.setCreatedAt(now);
        audit.setUpdatedAt(now);
    }

    @PreUpdate
    void onUpdate() { audit.setUpdatedAt(OffsetDateTime.now()); }

    // getters and setters
    public String getDynastyId() { return dynastyId; }
    public void setDynastyId(String v) { dynastyId = v; }
    public String getName() { return name; }
    public void setName(String v) { name = v; }
    public Integer getPeriodStart() { return periodStart; }
    public void setPeriodStart(Integer v) { periodStart = v; }
    public Integer getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(Integer v) { periodEnd = v; }
    public Set<Region> getRuledRegions() { return ruledRegions; }
    public void setRuledRegions(Set<Region> v) { ruledRegions = v; }
    public AuditFields getAudit() { return audit; }
    public void setCreatedBy(String v) { audit.setCreatedBy(v); }
}