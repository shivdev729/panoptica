package com.artifactexplorer.artifact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.artifactexplorer.artifact.entity.Artifact;
import com.artifactexplorer.common.ArtifactStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// artifact/repository/ArtifactRepository.java
@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, String> {
    List<Artifact> findByStatus(ArtifactStatus status);
    List<Artifact> findByMuseum_MuseumId(String museumId);
    List<Artifact> findByDynasty_DynastyId(String dynastyId);
    List<Artifact> findByType_TypeId(String typeId);
    List<Artifact> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a FROM Artifact a JOIN a.materials m WHERE m = :material")
    List<Artifact> findByMaterial(@Param("material") String material);

    @Query("""
        SELECT a FROM Artifact a
        WHERE (:status IS NULL OR a.status = :status)
          AND (:museumId IS NULL OR a.museum.museumId = :museumId)
          AND (:typeId IS NULL OR a.type.typeId = :typeId)
        """)
    Page<Artifact> findWithFilters(
        @Param("status") ArtifactStatus status,
        @Param("museumId") String museumId,
        @Param("typeId") String typeId,
        Pageable pageable
    );
}
