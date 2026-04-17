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
                        Pageable pageable);

        // artifact/repository/ArtifactRepository.java
        @Query("""
                        SELECT DISTINCT a FROM Artifact a
                        LEFT JOIN a.type        t
                        LEFT JOIN a.dynasty     d
                        LEFT JOIN a.museum      m
                        LEFT JOIN a.materials   mat
                        WHERE (:typeId    IS NULL OR t.typeId       = :typeId)
                          AND (:dynastyId IS NULL OR d.dynastyId    = :dynastyId)
                          AND (:dynastyName IS NULL OR d.name    = :dynastyName)
                          AND (:museumId  IS NULL OR m.museumId     = :museumId)
                          AND (:museumName IS NULL OR m.name    = :museumName)
                          AND (:material  IS NULL OR mat             = :material)
                          AND (:status    IS NULL OR a.status        = :status)
                          AND (:name      IS NULL OR LOWER(a.name)  LIKE LOWER(CONCAT('%', :name, '%')))
                          AND (:regionId  IS NULL OR EXISTS (
                                SELECT 1 FROM Dynasty d2
                                JOIN d2.ruledRegions r
                                WHERE d2 = d AND r.regionId = :regionId
                              ))
                          AND (:regionName  IS NULL OR EXISTS (
                                SELECT 1 FROM Dynasty d2
                                JOIN d2.ruledRegions r
                                WHERE d2 = d AND r.name= :regionName
                              ))
                        """)
        Page<Artifact> filter(
                        @Param("typeId") String typeId,
                        @Param("dynastyId") String dynastyId,
                        @Param("dynastyName") String dynastyName,
                        @Param("museumId") String museumId,
                        @Param("museumName") String museumName,
                        @Param("material") String material,
                        @Param("status") ArtifactStatus status,
                        @Param("name") String name,
                        @Param("regionId") String regionId,
                        @Param("regionName") String regionName,
                        Pageable pageable);
}
