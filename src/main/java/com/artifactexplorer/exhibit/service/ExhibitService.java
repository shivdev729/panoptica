package com.artifactexplorer.exhibit.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.artifactexplorer.admin.entity.AdminActionLog;
import com.artifactexplorer.admin.entity.AdminUser;
import com.artifactexplorer.admin.repository.AdminActionLogRepository;
import com.artifactexplorer.admin.repository.AdminUserRepository;
import com.artifactexplorer.artifact.entity.Artifact;
import com.artifactexplorer.artifact.repository.ArtifactRepository;
import com.artifactexplorer.common.ActionType;
import com.artifactexplorer.common.ExhibitStatus;
import com.artifactexplorer.exhibit.dto.ExhibitArtifactRequest;
import com.artifactexplorer.exhibit.dto.ExhibitArtifactResponse;
import com.artifactexplorer.exhibit.dto.ExhibitFilterRequest;
import com.artifactexplorer.exhibit.dto.ExhibitRequest;
import com.artifactexplorer.exhibit.dto.ExhibitResponse;
import com.artifactexplorer.exhibit.entity.Exhibit;
import com.artifactexplorer.exhibit.entity.ExhibitArtifact;
import com.artifactexplorer.exhibit.entity.ExhibitArtifactId;
import com.artifactexplorer.exhibit.entity.ExhibitRevision;
import com.artifactexplorer.exhibit.repository.ExhibitArtifactRepository;
import com.artifactexplorer.exhibit.repository.ExhibitRepository;
import com.artifactexplorer.exhibit.repository.ExhibitRevisionRepository;
import com.artifactexplorer.museum.repository.MuseumRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

// exhibit/service/ExhibitService.java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExhibitService {

    private final ExhibitRepository         exhibitRepo;
    private final ExhibitArtifactRepository exhibitArtifactRepo;
    private final ExhibitRevisionRepository revisionRepo;
    private final ArtifactRepository        artifactRepo;
    private final MuseumRepository          museumRepo;
    private final AdminUserRepository       adminUserRepo;
    private final AdminActionLogRepository  logRepo;

    // ── Exhibit queries ───────────────────────────────────────

    public Page<ExhibitResponse> filter(ExhibitFilterRequest f) {
        Pageable pageable = PageRequest.of(f.page(), f.size(),
                Sort.by("startsOn").ascending());

        if (f.from() != null && f.to() != null)
            return exhibitRepo.findUpcoming(f.from(), f.to(), pageable)
                    .map(ExhibitResponse::from);

        if (f.museumId() != null && f.status() != null)
            return exhibitRepo.findByStatusAndMuseum_MuseumId(
                    f.status(), f.museumId(), pageable)
                    .map(ExhibitResponse::from);

        if (f.museumId() != null)
            return exhibitRepo.findByMuseum_MuseumIdOrderByStartsOnAsc(
                    f.museumId(), pageable)
                    .map(ExhibitResponse::from);

        if (f.status() != null)
            return exhibitRepo.findByStatusOrderByStartsOnAsc(f.status(), pageable)
                    .map(ExhibitResponse::from);

        return exhibitRepo.findAll(pageable).map(ExhibitResponse::from);
    }

    public ExhibitResponse findById(String id) {
        return exhibitRepo.findById(id)
                .map(ExhibitResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Exhibit not found: " + id));
    }

    public List<ExhibitResponse> findActiveByMuseum(String museumId) {
        return exhibitRepo.findActiveByMuseum(museumId, LocalDate.now())
                .stream().map(ExhibitResponse::from).toList();
    }

    // ── Exhibit mutations ─────────────────────────────────────

    @Transactional
    public ExhibitResponse create(ExhibitRequest req, String adminId) {
        if (exhibitRepo.existsById(req.exhibitId()))
            throw new IllegalArgumentException("Exhibit ID already exists: " + req.exhibitId());
        validateDates(req.startsOn(), req.endsOn());

        Exhibit e = new Exhibit();
        applyFields(e, req);
        e.setCreatedBy(resolveAdmin(adminId));

        Exhibit saved = exhibitRepo.save(e);
        log(adminId, ActionType.CREATE, "exhibit", saved.getExhibitId(), null, "created");
        return ExhibitResponse.from(saved);
    }

    @Transactional
    public ExhibitResponse update(String id, ExhibitRequest req, String adminId) {
        Exhibit e = findExhibit(id);
        validateDates(req.startsOn(), req.endsOn());
        snapshotRevision(e, adminId, "full update");
        applyFields(e, req);
        e.setUpdatedBy(resolveAdmin(adminId));

        Exhibit saved = exhibitRepo.save(e);
        log(adminId, ActionType.UPDATE, "exhibit", id, null, "full update");
        return ExhibitResponse.from(saved);
    }

    @Transactional
    public ExhibitResponse patch(String id, Map<String, Object> fields, String adminId) {
        Exhibit e = findExhibit(id);
        snapshotRevision(e, adminId, "patch update");

        if (fields.containsKey("title"))
            e.setTitle((String) fields.get("title"));
        if (fields.containsKey("description"))
            e.setDescription((String) fields.get("description"));
        if (fields.containsKey("status"))
            e.setStatus(ExhibitStatus.valueOf((String) fields.get("status")));
        if (fields.containsKey("endsOn"))
            e.setEndsOn(LocalDate.parse((String) fields.get("endsOn")));

        e.setUpdatedBy(resolveAdmin(adminId));
        Exhibit saved = exhibitRepo.save(e);
        log(adminId, ActionType.UPDATE, "exhibit", id, null, "patched");
        return ExhibitResponse.from(saved);
    }

    @Transactional
    public void delete(String id, String adminId) {
        if (!exhibitRepo.existsById(id))
            throw new EntityNotFoundException("Exhibit not found: " + id);
        exhibitRepo.deleteById(id);
        log(adminId, ActionType.DELETE, "exhibit", id, null, "deleted");
    }

    // ── Status transitions ────────────────────────────────────

    @Transactional
    public ExhibitResponse publish(String id, String adminId) {
        return transition(id, adminId, ExhibitStatus.SCHEDULED, "published/scheduled");
    }

    @Transactional
    public ExhibitResponse cancel(String id, String adminId) {
        return transition(id, adminId, ExhibitStatus.CANCELLED, "cancelled");
    }

    @Transactional
    public ExhibitResponse close(String id, String adminId) {
        return transition(id, adminId, ExhibitStatus.CLOSED, "closed");
    }

    private ExhibitResponse transition(String id, String adminId,
                                       ExhibitStatus next, String note) {
        Exhibit e = findExhibit(id);
        snapshotRevision(e, adminId, note);
        e.setStatus(next);
        e.setUpdatedBy(resolveAdmin(adminId));
        Exhibit saved = exhibitRepo.save(e);
        log(adminId, ActionType.UPDATE, "exhibit", id, null, note);
        return ExhibitResponse.from(saved);
    }

    // ── Artifact lineup ───────────────────────────────────────

    public List<ExhibitArtifactResponse> getLineup(String exhibitId) {
        return exhibitArtifactRepo
                .findByExhibit_ExhibitIdOrderByDisplayOrderAsc(exhibitId)
                .stream().map(ExhibitArtifactResponse::from).toList();
    }

    @Transactional
    public ExhibitArtifactResponse addArtifact(String exhibitId,
                                                ExhibitArtifactRequest req,
                                                String adminId) {
        Exhibit exhibit = findExhibit(exhibitId);

        if (exhibit.getStatus() == ExhibitStatus.CLOSED ||
            exhibit.getStatus() == ExhibitStatus.CANCELLED)
            throw new IllegalStateException(
                "Cannot modify lineup of a " + exhibit.getStatus() + " exhibit");

        Artifact artifact = artifactRepo.findById(req.artifactId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Artifact not found: " + req.artifactId()));

        ExhibitArtifactId eaId = new ExhibitArtifactId(exhibitId, req.artifactId());
        if (exhibitArtifactRepo.existsById(eaId))
            throw new IllegalArgumentException("Artifact already in this exhibit");

        ExhibitArtifact ea = new ExhibitArtifact();
        ea.setId(eaId);
        ea.setExhibit(exhibit);
        ea.setArtifact(artifact);
        ea.setDisplayOrder(req.displayOrder());
        ea.setNotes(req.notes());
        ea.setAddedBy(resolveAdmin(adminId));

        ExhibitArtifact saved = exhibitArtifactRepo.save(ea);
        log(adminId, ActionType.UPDATE, "exhibit", exhibitId, null,
                "added artifact " + req.artifactId());
        return ExhibitArtifactResponse.from(saved);
    }

    @Transactional
    public ExhibitArtifactResponse updateArtifactEntry(String exhibitId,
                                                        String artifactId,
                                                        ExhibitArtifactRequest req,
                                                        String adminId) {
        ExhibitArtifactId eaId = new ExhibitArtifactId(exhibitId, artifactId);
        ExhibitArtifact ea = exhibitArtifactRepo.findById(eaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Artifact not in exhibit: " + artifactId));
        ea.setDisplayOrder(req.displayOrder());
        ea.setNotes(req.notes());
        return ExhibitArtifactResponse.from(exhibitArtifactRepo.save(ea));
    }

    @Transactional
    public void removeArtifact(String exhibitId, String artifactId, String adminId) {
        ExhibitArtifactId eaId = new ExhibitArtifactId(exhibitId, artifactId);
        if (!exhibitArtifactRepo.existsById(eaId))
            throw new EntityNotFoundException("Artifact not in exhibit: " + artifactId);
        exhibitArtifactRepo.deleteById(eaId);
        log(adminId, ActionType.UPDATE, "exhibit", exhibitId, null,
                "removed artifact " + artifactId);
    }

    // ── Revisions ─────────────────────────────────────────────

    public List<ExhibitRevision> getRevisions(String exhibitId) {
        return revisionRepo.findByExhibit_ExhibitIdOrderByRevisedAtDesc(exhibitId);
    }

    // ── Helpers ───────────────────────────────────────────────

    private Exhibit findExhibit(String id) {
        return exhibitRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exhibit not found: " + id));
    }

    private void applyFields(Exhibit e, ExhibitRequest req) {
        e.setExhibitId(req.exhibitId());
        e.setTitle(req.title());
        e.setDescription(req.description());
        e.setStartsOn(req.startsOn());
        e.setEndsOn(req.endsOn());
        e.setRecurring(req.isRecurring());
        e.setStatus(req.status() != null ? req.status() : ExhibitStatus.DRAFT);
        e.setMuseum(museumRepo.findById(req.museumId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Museum not found: " + req.museumId())));
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (end != null && end.isBefore(start))
            throw new IllegalArgumentException("ends_on must be on or after starts_on");
    }

    private void snapshotRevision(Exhibit e, String adminId, String note) {
        ExhibitRevision rev = new ExhibitRevision();
        rev.setExhibit(e);
        rev.setRevisedBy(resolveAdmin(adminId));
        rev.setChangeNote(note);
        rev.setSnapshot(Map.of(
            "title",       String.valueOf(e.getTitle()),
            "description", String.valueOf(e.getDescription()),
            "status",      String.valueOf(e.getStatus()),
            "museumId",    e.getMuseum().getMuseumId(),
            "startsOn",    String.valueOf(e.getStartsOn()),
            "endsOn",      String.valueOf(e.getEndsOn())
        ));
        revisionRepo.save(rev);
    }

    private AdminUser resolveAdmin(String adminId) {
        return adminUserRepo.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Admin not found: " + adminId));
    }

    private void log(String adminId, ActionType action, String entityType,
                     String entityId, Map<String, Object> diff, String note) {
        AdminActionLog entry = new AdminActionLog();
        adminUserRepo.findById(adminId).ifPresent(entry::setAdmin);
        entry.setAction(action);
        entry.setEntityType(entityType);
        entry.setEntityId(entityId);
        entry.setDiff(diff);
        entry.setNote(note);
        logRepo.save(entry);
    }
} 