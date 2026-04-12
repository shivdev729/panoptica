package com.artifactexplorer.artifact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artifactexplorer.artifact.entity.Depicts;
import com.artifactexplorer.artifact.entity.DepictsId;

import java.util.List;

// artifact/repository/DepictsRepository.java
@Repository
public interface DepictsRepository extends JpaRepository<Depicts, DepictsId> {
    List<Depicts> findByArtifact_ArtifactId(String artifactId);
    List<Depicts> findByArtifact_ArtifactIdIn(List<String> artifactIds);
}
