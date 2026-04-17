package com.artifactexplorer.artifact.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import com.artifactexplorer.artifact.dto.ArtifactFilterRequest;
import com.artifactexplorer.artifact.dto.ArtifactRequest;
import com.artifactexplorer.artifact.dto.ArtifactResponse;
import com.artifactexplorer.artifact.dto.ArtifactTypeRequest;
import com.artifactexplorer.artifact.dto.ArtifactTypeResponse;
import com.artifactexplorer.artifact.entity.ArtifactType;
import com.artifactexplorer.artifact.entity.ArtifactRevision;
import com.artifactexplorer.artifact.service.ArtifactService;
import com.artifactexplorer.artifact.repository.ArtifactTypeRepository;
import com.artifactexplorer.common.IdGenerator;

@RestController
@CrossOrigin
@RequestMapping("/api/artifacts")
@RequiredArgsConstructor
public class ArtifactController {

    private final ArtifactService artifactService;
    private final ArtifactTypeRepository typeRepo;
    private final IdGenerator idGenerator;

    // ── Artifact CRUD ──────────────────────────────────────────

    /**
     * Handles the list and filtering of artifacts.
     * BindingResult is used to prevent 400 errors from crashing and to log the cause.
     */
    @GetMapping
    public ResponseEntity<?> list(
            @ModelAttribute ArtifactFilterRequest filter, 
            BindingResult bindingResult) { 

        if (bindingResult.hasErrors()) {
            // Check your terminal/IDE console to see which field is failing!
            bindingResult.getAllErrors().forEach(error -> 
                System.out.println("Binding Error: " + error.toString()));
            return ResponseEntity.badRequest().body("Invalid request parameters. Check your DTO fields.");
        }

        return ResponseEntity.ok(artifactService.filter(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtifactResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(artifactService.findById(id));
    }

    @GetMapping("/{id}/revisions")
    public ResponseEntity<List<ArtifactRevision>> revisions(@PathVariable String id) {
        return ResponseEntity.ok(artifactService.revisions(id));
    }

    @PostMapping
    public ResponseEntity<ArtifactResponse> create(
            @Valid @RequestBody ArtifactRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(artifactService.create(req, (String) auth.getPrincipal()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtifactResponse> update(
            @PathVariable String id,
            @Valid @RequestBody ArtifactRequest req, Authentication auth) {
        return ResponseEntity.ok(artifactService.update(id, req, (String) auth.getPrincipal()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ArtifactResponse> patch(
            @PathVariable String id,
            @RequestBody Map<String, Object> fields, Authentication auth) {
        return ResponseEntity.ok(artifactService.patch(id, fields, (String) auth.getPrincipal()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        artifactService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Artifact Types (nested under artifacts) ────────────────

    @GetMapping("/types")
    public ResponseEntity<List<ArtifactTypeResponse>> listTypes(
            @RequestParam(required = false) String parentId) {
        List<ArtifactType> types = (parentId != null)
                ? typeRepo.findByParentType_TypeId(parentId)
                : typeRepo.findByParentTypeIsNull();
        return ResponseEntity.ok(types.stream().map(ArtifactTypeResponse::from).toList());
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable String id) {
        if (!typeRepo.existsById(id))
            throw new EntityNotFoundException("ArtifactType not found: " + id);
        typeRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
