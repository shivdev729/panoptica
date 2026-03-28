package com.artifactexplorer.deity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.artifactexplorer.deity.entity.Deity;
import java.util.List;

// deity/repository/DeityRepository.java
@Repository
public interface DeityRepository extends JpaRepository<Deity, String> {
    List<Deity> findByNameContainingIgnoreCase(String name);

    @Query("SELECT d FROM Deity d JOIN d.traditions t WHERE t = :tradition")
    List<Deity> findByTradition(@Param("tradition") String tradition);
}