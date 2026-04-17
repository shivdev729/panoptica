package com.artifactexplorer.exhibit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.artifactexplorer.exhibit.dto.ExhibitArtifactRequest;
import com.artifactexplorer.exhibit.dto.ExhibitArtifactResponse;
import com.artifactexplorer.exhibit.dto.ExhibitFilterRequest;
import com.artifactexplorer.exhibit.dto.ExhibitRequest;
import com.artifactexplorer.exhibit.dto.ExhibitResponse;
import com.artifactexplorer.exhibit.entity.ExhibitRevision;
import com.artifactexplorer.exhibit.service.ExhibitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// exhibit/controller/ExhibitController.java
@RestController
@RequestMapping("/api/exhibits")
@RequiredArgsConstructor
public class ExhibitController {

    private final ExhibitService service;

    // ── Public reads ──────────────────────────────────────────

    @GetMapping
    public ResponseEntity<Page<ExhibitResponse>> list(
            @ModelAttribute ExhibitFilterRequest filter) {
        return ResponseEntity.ok(service.filter(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExhibitResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ExhibitResponse>> active(
            @RequestParam String museumId) {
        return ResponseEntity.ok(service.findActiveByMuseum(museumId));
    }

    @GetMapping("/{id}/lineup")
    public ResponseEntity<List<ExhibitArtifactResponse>> lineup(@PathVariable String id) {
        return ResponseEntity.ok(service.getLineup(id));
    }

    @GetMapping("/{id}/revisions")
    public ResponseEntity<List<ExhibitRevision>> revisions(@PathVariable String id) {
        return ResponseEntity.ok(service.getRevisions(id));
    }

    // ── Admin writes ──────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ExhibitResponse> create(
            @Valid @RequestBody ExhibitRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(req, (String) auth.getPrincipal()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExhibitResponse> update(
            @PathVariable String id,
            @Valid @RequestBody ExhibitRequest req,
            Authentication auth) {
        return ResponseEntity.ok(service.update(id, req, (String) auth.getPrincipal()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExhibitResponse> patch(
            @PathVariable String id,
            @RequestBody Map<String, Object> fields,
            Authentication auth) {
        return ResponseEntity.ok(service.patch(id, fields, (String) auth.getPrincipal()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            Authentication auth) {
        service.delete(id, (String) auth.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    // ── Status transitions ────────────────────────────────────

    @PostMapping("/{id}/publish")
    public ResponseEntity<ExhibitResponse> publish(
            @PathVariable String id, Authentication auth) {
        return ResponseEntity.ok(service.publish(id, (String) auth.getPrincipal()));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ExhibitResponse> cancel(
            @PathVariable String id, Authentication auth) {
        return ResponseEntity.ok(service.cancel(id, (String) auth.getPrincipal()));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<ExhibitResponse> close(
            @PathVariable String id, Authentication auth) {
        return ResponseEntity.ok(service.close(id, (String) auth.getPrincipal()));
    }

    // ── Artifact lineup management ────────────────────────────

    @PostMapping("/{id}/lineup")
    public ResponseEntity<ExhibitArtifactResponse> addArtifact(
            @PathVariable String id,
            @Valid @RequestBody ExhibitArtifactRequest req,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addArtifact(id, req, (String) auth.getPrincipal()));
    }

    @PutMapping("/{id}/lineup/{artifactId}")
    public ResponseEntity<ExhibitArtifactResponse> updateArtifactEntry(
            @PathVariable String id,
            @PathVariable String artifactId,
            @Valid @RequestBody ExhibitArtifactRequest req,
            Authentication auth) {
        return ResponseEntity.ok(
                service.updateArtifactEntry(id, artifactId, req, (String) auth.getPrincipal()));
    }

    @DeleteMapping("/{id}/lineup/{artifactId}")
    public ResponseEntity<Void> removeArtifact(
            @PathVariable String id,
            @PathVariable String artifactId,
            Authentication auth) {
        service.removeArtifact(id, artifactId, (String) auth.getPrincipal());
        return ResponseEntity.noContent().build();
    }
} 
