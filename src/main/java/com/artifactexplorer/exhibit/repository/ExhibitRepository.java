package com.artifactexplorer.exhibit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.artifactexplorer.exhibit.entity.Exhibit;
import com.artifactexplorer.common.ExhibitStatus;

import java.util.List;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
// debug this line (sus)
import org.springframework.stereotype.Repository;

// exhibit/repository/ExhibitRepository.java
@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, String> {
    List<Exhibit> findByStatus(ExhibitStatus status);

    List<Exhibit> findByMuseum_MuseumId(String museumId);

    // exhibit/repository/ExhibitRepository.java — additions
    Page<Exhibit> findByStatusOrderByStartsOnAsc(ExhibitStatus status, Pageable pageable);

    Page<Exhibit> findByMuseum_MuseumIdOrderByStartsOnAsc(String museumId, Pageable pageable);

    @Query("""
            SELECT e FROM Exhibit e
            WHERE e.startsOn BETWEEN :from AND :to
            ORDER BY e.startsOn ASC
            """)
    Page<Exhibit> findUpcoming(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable);

    // Active exhibits at a museum right now
    @Query("""
            SELECT e FROM Exhibit e
            WHERE e.museum.museumId = :museumId
              AND e.status = 'ACTIVE'
              AND e.startsOn <= :today
              AND (e.endsOn IS NULL OR e.endsOn >= :today)
            """)
    List<Exhibit> findActiveByMuseum(
            @Param("museumId") String museumId,
            @Param("today") LocalDate today);

    // Upcoming exhibits in a date window
    @Query("""
            SELECT e FROM Exhibit e
            WHERE e.startsOn BETWEEN :from AND :to
            ORDER BY e.startsOn ASC
            """)
    List<Exhibit> findUpcoming(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    Page<Exhibit> findByStatusAndMuseum_MuseumId(
            ExhibitStatus status, String museumId, Pageable pageable);
}
