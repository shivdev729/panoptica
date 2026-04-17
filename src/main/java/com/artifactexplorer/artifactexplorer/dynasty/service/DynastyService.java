package com.artifactexplorer.dynasty.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.artifactexplorer.dynasty.dto.DynastyRequest;
import com.artifactexplorer.dynasty.dto.DynastyResponse;
import com.artifactexplorer.dynasty.entity.Dynasty;
import com.artifactexplorer.dynasty.repository.DynastyRepository;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;
import com.artifactexplorer.common.IdGenerator;

// dynasty/service/DynastyService.java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DynastyService {

    private final DynastyRepository repo;
    private final IdGenerator idGenerator;

    public List<DynastyResponse> findAll() {
        return repo.findAll().stream().map(DynastyResponse::from).toList();
    }

    public DynastyResponse findById(String id) {
        return repo.findById(id)
                .map(DynastyResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Dynasty not found: " + id));
    }

    public List<DynastyResponse> search(String name) {
        return repo.findByNameContainingIgnoreCase(name)
                .stream().map(DynastyResponse::from).toList();
    }

    public List<DynastyResponse> findByPeriod(int start, int end) {
        return repo.findByPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(start, end)
                .stream().map(DynastyResponse::from).toList();
    }

    @Transactional
    public DynastyResponse create(DynastyRequest req, String createdBy) {
        
        Dynasty d = new Dynasty();
        d.setDynastyId(idGenerator.generate("DYN"));
        d.setName(req.name());
        d.setPeriodStart(req.periodStart());
        d.setPeriodEnd(req.periodEnd());
        d.setCreatedBy(createdBy);
        return DynastyResponse.from(repo.save(d));
    }

    @Transactional
    public DynastyResponse update(String id, DynastyRequest req) {
        Dynasty d = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dynasty not found: " + id));
        d.setName(req.name());
        d.setPeriodStart(req.periodStart());
        d.setPeriodEnd(req.periodEnd());
        return DynastyResponse.from(repo.save(d));
    }

    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Dynasty not found: " + id);
        repo.deleteById(id);
    }
}
