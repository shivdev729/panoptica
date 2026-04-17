package com.artifactexplorer.museum.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artifactexplorer.museum.entity.Museum;

// museum/repository/MuseumRepository.java
@Repository
public interface MuseumRepository extends JpaRepository<Museum, String> {
    List<Museum> findByNameContainingIgnoreCase(String name);
    List<Museum> findByLocation(String location);
}
