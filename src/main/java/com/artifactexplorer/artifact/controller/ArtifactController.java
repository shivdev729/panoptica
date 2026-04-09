package com.artifactexplorer.artifact.controller;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

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


// artifact/controller/ArtifactController.java
@RestController
@RequestMapping("/api/artifacts")
@RequiredArgsConstructor
public class ArtifactController {

    private final ArtifactService    artifactService;
    private final ArtifactTypeRepository typeRepo;
    private final IdGenerator idGenerator;

    // ── Artifact CRUD ──────────────────────────────────────────

    @GetMapping
    public ResponseEntity<Page<ArtifactResponse>> list(
            @ModelAttribute ArtifactFilterRequest filter) {
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
            @Valid @RequestBody ArtifactRequest req,Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artifactService.create(req,(String) auth.getPrincipal()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtifactResponse> update(
            @PathVariable String id,
            @Valid @RequestBody ArtifactRequest req,Authentication auth) {
        return ResponseEntity.ok(artifactService.update(id, req,(String) auth.getPrincipal()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ArtifactResponse> patch(
            @PathVariable String id,
            @RequestBody Map<String, Object> fields,Authentication auth) {
        return ResponseEntity.ok(artifactService.patch(id, fields,(String) auth.getPrincipal()));
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

    @GetMapping("/types/{id}")
    public ResponseEntity<ArtifactTypeResponse> getType(@PathVariable String id) {
        return typeRepo.findById(id)
                .map(t -> ResponseEntity.ok(ArtifactTypeResponse.from(t)))
                .orElseThrow(() -> new EntityNotFoundException("ArtifactType not found: " + id));
    }

    @PostMapping("/types")
    public ResponseEntity<ArtifactTypeResponse> createType(
            @Valid @RequestBody ArtifactTypeRequest req) {
        ArtifactType t = new ArtifactType();
        t.setTypeId(idGenerator.generate("TYP"));
        t.setName(req.name());
        if (req.parentTypeId() != null)
            t.setParentType(typeRepo.findById(req.parentTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent type not found")));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ArtifactTypeResponse.from(typeRepo.save(t)));
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<ArtifactTypeResponse> updateType(
            @PathVariable String id,
            @Valid @RequestBody ArtifactTypeRequest req) {
        ArtifactType t = typeRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ArtifactType not found: " + id));
        t.setName(req.name());
        if (req.parentTypeId() != null)
            t.setParentType(typeRepo.findById(req.parentTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent type not found")));
        return ResponseEntity.ok(ArtifactTypeResponse.from(typeRepo.save(t)));
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable String id) {
        if (!typeRepo.existsById(id))
            throw new EntityNotFoundException("ArtifactType not found: " + id);
        typeRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
