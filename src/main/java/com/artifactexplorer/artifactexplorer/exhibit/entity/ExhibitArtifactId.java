package com.artifactexplorer.exhibit.entity;

import jakarta.persistence.*;
import java.io.Serializable;

// exhibit/entity/ExhibitArtifactId.java
@Embeddable
public class ExhibitArtifactId implements Serializable {

    @Column(name = "exhibit_id", length = 20)
    private String exhibitId;

    @Column(name = "artifact_id", length = 20)
    private String artifactId;

    // equals, hashCode, constructors
    public ExhibitArtifactId() {}
    public ExhibitArtifactId(String exhibitId, String artifactId) {
        this.exhibitId = exhibitId;
        this.artifactId = artifactId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExhibitArtifactId that = (ExhibitArtifactId) o;
        return exhibitId.equals(that.exhibitId) && artifactId.equals(that.artifactId);
    }
    @Override
    public int hashCode() {
        return exhibitId.hashCode() * 31 + artifactId.hashCode();
    }
    
}