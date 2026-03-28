package com.artifactexplorer.artifact.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class DepictsId implements Serializable {
    @Column(name = "artifact_id", length = 20)
    private String artifactId;
    @Column(name = "deity_id", length = 20)
    private String deityId;
    // equals, hashCode
}