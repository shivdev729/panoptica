package com.artifactexplorer.artifact.service;

import com.artifactexplorer.artifact.dto.ArtifactFilterRequest;
import com.artifactexplorer.artifact.dto.ArtifactPatchRequest;
import com.artifactexplorer.artifact.dto.ArtifactRequest;
import com.artifactexplorer.artifact.dto.ArtifactResponse;
import com.artifactexplorer.artifact.entity.Artifact;
import com.artifactexplorer.artifact.entity.ArtifactRevision;
import com.artifactexplorer.artifact.entity.Depicts;
import com.artifactexplorer.artifact.repository.ArtifactRepository;
import com.artifactexplorer.artifact.repository.ArtifactRevisionRepository;
import com.artifactexplorer.artifact.repository.ArtifactTypeRepository;
import com.artifactexplorer.artifact.repository.DepictsRepository;
import com.artifactexplorer.dynasty.repository.DynastyRepository;
import com.artifactexplorer.museum.repository.MuseumRepository;
import com.artifactexplorer.common.ArtifactStatus;
import com.artifactexplorer.common.IdGenerator;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtifactService {

    private final ArtifactRepository         artifactRepo;
    private final ArtifactTypeRepository     typeRepo;
    private final ArtifactRevisionRepository revisionRepo;
    private final DynastyRepository          dynastyRepo;
    private final MuseumRepository           museumRepo;
    private final DepictsRepository          depictsRepo;
    private final IdGenerator                idGenerator;

    // ── Queries ───────────────────────────────────────────────

    public Page<ArtifactResponse> filter(ArtifactFilterRequest f) {
        Pageable pageable = PageRequest.of(f.page(), f.size(), Sort.by("name"));

        Page<Artifact> page = artifactRepo.filter(
                nullIfBlank(f.typeId()),   nullIfBlank(f.dynastyId()),
                nullIfBlank(f.museumId()), nullIfBlank(f.material()),
                f.status(),                nullIfBlank(f.name()),
                nullIfBlank(f.regionId()), pageable
        );

        // batch fetch all depicts for this page — avoids N+1
        List<String> ids = page.getContent().stream()
                .map(Artifact::getArtifactId).toList();

        Map<String, List<Depicts>> depictsMap = depictsRepo
                .findByArtifact_ArtifactIdIn(ids)
                .stream()
                .collect(Collectors.groupingBy(d -> d.getArtifact().getArtifactId()));

        return page.map(a -> ArtifactResponse.from(
                a,
                depictsMap.getOrDefault(a.getArtifactId(), List.of())
        ));
    }

    public ArtifactResponse findById(String id) {
        Artifact a = artifactRepo.findByArtifactId(id)
                .orElseThrow(() -> new EntityNotFoundException("Artifact not found: " + id));
        List<Depicts> depictsList = depictsRepo.findByArtifact_ArtifactId(id);
        return ArtifactResponse.from(a, depictsList);
    }

    public List<ArtifactRevision> revisions(String id) {
        return revisionRepo.findByArtifact_ArtifactIdOrderByRevisedAtDesc(id);
    }

    // ── Mutations ─────────────────────────────────────────────

    @Transactional
    public ArtifactResponse create(ArtifactRequest req, String adminId) {
        Artifact a = new Artifact();
        a.setArtifactId(idGenerator.generate("ART"));   // ← ID set once, only on create
        applyFields(a, req);
        a.setCreatedBy(adminId);
        a.setUpdatedBy(adminId);
        return ArtifactResponse.from(artifactRepo.save(a), List.of());
    }

    @Transactional
    public ArtifactResponse put(String id, ArtifactRequest req, String adminId) {
        Artifact a = artifactRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artifact not found: " + id));
        snapshotRevision(a, adminId);
        applyFields(a, req);                             // ← ID never touched
        a.setUpdatedBy(adminId);
        List<Depicts> depictsList = depictsRepo.findByArtifact_ArtifactId(id);
        return ArtifactResponse.from(artifactRepo.save(a), depictsList);
    }

    @Transactional
    public ArtifactResponse patch(String id, ArtifactPatchRequest req, String adminId) {
        Artifact a = artifactRepo.findByArtifactId(id)
                .orElseThrow(() -> new EntityNotFoundException("Artifact not found: " + id));
        snapshotRevision(a, adminId);

        req.name()       .ifPresent(a::setName);
        req.description().ifPresent(a::setDescription);
        req.status()     .ifPresent(a::setStatus);
        req.materials()  .ifPresent(a::setMaterials);

        req.typeId().ifPresent(typeId ->
            a.setType(typeRepo.findById(typeId)
                    .orElseThrow(() -> new EntityNotFoundException("Type not found: " + typeId))));

        req.dynastyId().ifPresent(dynastyId ->
            a.setDynasty(dynastyRepo.findById(dynastyId)
                    .orElseThrow(() -> new EntityNotFoundException("Dynasty not found: " + dynastyId))));

        req.museumId().ifPresent(museumId ->
            a.setMuseum(museumRepo.findById(museumId)
                    .orElseThrow(() -> new EntityNotFoundException("Museum not found: " + museumId))));

        a.setUpdatedBy(adminId);

        List<Depicts> depictsList = depictsRepo.findByArtifact_ArtifactId(id);
        return ArtifactResponse.from(artifactRepo.save(a), depictsList);
    }

    @Transactional
    public void delete(String id) {
        if (!artifactRepo.existsById(id))
            throw new EntityNotFoundException("Artifact not found: " + id);
        artifactRepo.deleteById(id);
    }

    // ── Helpers ───────────────────────────────────────────────

    // applies all mutable fields — never touches artifactId
    private void applyFields(Artifact a, ArtifactRequest req) {
        a.setName(req.name());
        a.setDescription(req.description());
        a.setStatus(req.status() != null ? req.status() : ArtifactStatus.DRAFT);
        a.setMaterials(req.materials() != null ? req.materials() : new HashSet<>());

        if (req.typeId() != null)
            a.setType(typeRepo.findById(req.typeId())
                    .orElseThrow(() -> new EntityNotFoundException("ArtifactType not found: " + req.typeId())));
        else
            a.setType(null);

        if (req.dynastyId() != null)
            a.setDynasty(dynastyRepo.findById(req.dynastyId())
                    .orElseThrow(() -> new EntityNotFoundException("Dynasty not found: " + req.dynastyId())));
        else
            a.setDynasty(null);

        if (req.museumId() != null)
            a.setMuseum(museumRepo.findById(req.museumId())
                    .orElseThrow(() -> new EntityNotFoundException("Museum not found: " + req.museumId())));
        else
            a.setMuseum(null);
    }

    private void snapshotRevision(Artifact a, String revisedBy) {
        ArtifactRevision rev = new ArtifactRevision();
        rev.setArtifact(a);
        rev.setRevisedBy(revisedBy);
        rev.setSnapshot(Map.of(
            "name",        String.valueOf(a.getName()),
            "description", String.valueOf(a.getDescription()),
            "status",      String.valueOf(a.getStatus()),
            "typeId",      a.getType()    != null ? a.getType().getTypeId()       : "",
            "dynastyId",   a.getDynasty() != null ? a.getDynasty().getDynastyId() : "",
            "museumId",    a.getMuseum()  != null ? a.getMuseum().getMuseumId()   : ""
        ));
        revisionRepo.save(rev);
    }

    private static String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}