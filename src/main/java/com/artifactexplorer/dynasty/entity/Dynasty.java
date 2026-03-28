package com.artifactexplorer.dynasty.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;

import com.artifactexplorer.common.AuditFields;

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

    @Embedded
    private AuditFields audit = new AuditFields();

    // getters, setters
}