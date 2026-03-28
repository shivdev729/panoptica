package com.artifactexplorer.dynasty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artifactexplorer.dynasty.entity.Dynasty;

import java.util.List;
// dynasty/repository/DynastyRepository.java
@Repository
public interface DynastyRepository extends JpaRepository<Dynasty, String> {
    List<Dynasty> findByNameContainingIgnoreCase(String name);
    List<Dynasty> findByPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(
        int start, int end);
}