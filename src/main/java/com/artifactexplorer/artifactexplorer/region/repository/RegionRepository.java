package com.artifactexplorer.region.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import com.artifactexplorer.region.entity.Region;

import java.util.List;

// region/repository/RegionRepository.java
@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
    List<Region> findByNameContainingIgnoreCase(String name);

    @Query("SELECT r FROM Region r JOIN r.modernStates s WHERE s = :state")
    List<Region> findByModernState(@Param("state") String state);
}
