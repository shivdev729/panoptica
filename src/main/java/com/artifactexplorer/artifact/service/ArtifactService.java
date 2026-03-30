package com.artifactexplorer.artifact.service;

import com.artifactexplorer.artifact.dto.ArtifactFilterRequest;
import com.artifactexplorer.artifact.dto.ArtifactRequest;
import com.artifactexplorer.artifact.dto.ArtifactResponse;
import com.artifactexplorer.artifact.entity.Artifact;
import com.artifactexplorer.artifact.entity.ArtifactRevision;
import com.artifactexplorer.artifact.repository.ArtifactRepository;
import com.artifactexplorer.artifact.repository.ArtifactRevisionRepository;
import com.artifactexplorer.artifact.repository.ArtifactTypeRepository;
import com.artifactexplorer.dynasty.repository.DynastyRepository;
import com.artifactexplorer.museum.repository.MuseumRepository;
import com.artifactexplorer.common.ArtifactStatus;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.HashSet;

// artifact/service/ArtifactService.java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtifactService {

    private final ArtifactRepository        artifactRepo;
    private final ArtifactTypeRepository    typeRepo;
    private final ArtifactRevisionRepository revisionRepo;
    private final DynastyRepository         dynastyRepo;
    private final MuseumRepository          museumRepo;

    public Page<ArtifactResponse> filter(ArtifactFilterRequest f) {
        Pageable pageable = PageRequest.of(f.page(), f.size(), Sort.by("name"));
        return artifactRepo.filter(
                nullIfBlank(f.typeId()), nullIfBlank(f.dynastyId()),
                nullIfBlank(f.museumId()), nullIfBlank(f.material()),
                f.status(), nullIfBlank(f.name()),
                nullIfBlank(f.regionId()), pageable
        ).map(ArtifactResponse::from);
    }

    public ArtifactResponse findById(String id) {
        return artifactRepo.findById(id)
                .map(ArtifactResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Artifact not found: " + id));
    }

    @Transactional
    public ArtifactResponse create(ArtifactRequest req) {
        if (artifactRepo.existsById(req.artifactId()))
            throw new IllegalArgumentException("Artifact ID already exists: " + req.artifactId());
        return ArtifactResponse.from(artifactRepo.save(buildArtifact(new Artifact(), req)));
    }

    @Transactional
    public ArtifactResponse update(String id, ArtifactRequest req) {
        Artifact a = artifactRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artifact not found: " + id));
        snapshotRevision(a);
        return ArtifactResponse.from(artifactRepo.save(buildArtifact(a, req)));
    }

    @Transactional
    public ArtifactResponse patch(String id, Map<String, Object> fields) {
        Artifact a = artifactRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artifact not found: " + id));
        snapshotRevision(a);
        if (fields.containsKey("status"))
            a.setStatus(ArtifactStatus.valueOf((String) fields.get("status")));
        if (fields.containsKey("name"))
            a.setName((String) fields.get("name"));
        if (fields.containsKey("description"))
            a.setDescription((String) fields.get("description"));
        return ArtifactResponse.from(artifactRepo.save(a));
    }

    @Transactional
    public void delete(String id) {
        if (!artifactRepo.existsById(id))
            throw new EntityNotFoundException("Artifact not found: " + id);
        artifactRepo.deleteById(id);
    }

    public List<ArtifactRevision> revisions(String id) {
        return revisionRepo.findByArtifact_ArtifactIdOrderByRevisedAtDesc(id);
    }

    // ── helpers ──────────────────────────────────────────────

    private Artifact buildArtifact(Artifact a, ArtifactRequest req) {
        a.setArtifactId(req.artifactId());
        a.setName(req.name());
        a.setDescription(req.description());
        a.setStatus(req.status() != null ? req.status() : ArtifactStatus.DRAFT);
        a.setMaterials(req.materials() != null ? req.materials() : new HashSet<>());
        if (req.typeId() != null)
            a.setType(typeRepo.findById(req.typeId())
                    .orElseThrow(() -> new EntityNotFoundException("ArtifactType not found: " + req.typeId())));
        if (req.dynastyId() != null)
            a.setDynasty(dynastyRepo.findById(req.dynastyId())
                    .orElseThrow(() -> new EntityNotFoundException("Dynasty not found: " + req.dynastyId())));
        if (req.museumId() != null)
            a.setMuseum(museumRepo.findById(req.museumId())
                    .orElseThrow(() -> new EntityNotFoundException("Museum not found: " + req.museumId())));
        return a;
    }

    private void snapshotRevision(Artifact a) {
        ArtifactRevision rev = new ArtifactRevision();
        rev.setArtifact(a);
        rev.setRevisedBy("system");   // replace with SecurityContext principal
        rev.setSnapshot(Map.of(
            "name", String.valueOf(a.getName()),
            "description", String.valueOf(a.getDescription()),
            "status", String.valueOf(a.getStatus()),
            "typeId", a.getType() != null ? a.getType().getTypeId() : "",
            "dynastyId", a.getDynasty() != null ? a.getDynasty().getDynastyId() : "",
            "museumId", a.getMuseum()  != null ? a.getMuseum().getMuseumId()   : ""
        ));
        revisionRepo.save(rev);
    }

    private static String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}