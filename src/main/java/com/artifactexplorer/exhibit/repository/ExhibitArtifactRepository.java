package com.artifactexplorer.exhibit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.artifactexplorer.exhibit.entity.ExhibitArtifact;
import com.artifactexplorer.exhibit.entity.ExhibitArtifactId;
import com.artifactexplorer.common.ExhibitStatus;

import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface ExhibitArtifactRepository
        extends JpaRepository<ExhibitArtifact, ExhibitArtifactId> {

    List<ExhibitArtifact> findByExhibit_ExhibitIdOrderByDisplayOrderAsc(String exhibitId);
    List<ExhibitArtifact> findByArtifact_ArtifactId(String artifactId);
    boolean existsByArtifact_ArtifactIdAndExhibit_Status(String artifactId, ExhibitStatus status);
}
