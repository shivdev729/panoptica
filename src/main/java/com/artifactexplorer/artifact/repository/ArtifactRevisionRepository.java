package com.artifactexplorer.artifact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artifactexplorer.artifact.entity.ArtifactRevision;
import java.util.List;
import java.util.Optional;
// artifact/repository/ArtifactRevisionRepository.java
@Repository
public interface ArtifactRevisionRepository extends JpaRepository<ArtifactRevision, Integer> {
    List<ArtifactRevision> findByArtifact_ArtifactIdOrderByRevisedAtDesc(String artifactId);
    Optional<ArtifactRevision> findTopByArtifact_ArtifactIdOrderByRevisedAtDesc(String artifactId);
}