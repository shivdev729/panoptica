package com.artifactexplorer.exhibit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.artifactexplorer.exhibit.entity.ExhibitRevision;
import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Repository;

// exhibit/repository/ExhibitRevisionRepository.java
@Repository
public interface ExhibitRevisionRepository extends JpaRepository<ExhibitRevision, Integer> {
    List<ExhibitRevision> findByExhibit_ExhibitIdOrderByRevisedAtDesc(String exhibitId);
    Optional<ExhibitRevision> findTopByExhibit_ExhibitIdOrderByRevisedAtDesc(String exhibitId);
}