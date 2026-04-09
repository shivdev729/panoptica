package com.artifactexplorer.region.service;

import java.util.HashSet;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.artifactexplorer.region.dto.RegionRequest;
import com.artifactexplorer.region.dto.RegionResponse;
import com.artifactexplorer.region.repository.RegionRepository;
import com.artifactexplorer.region.entity.Region;
import com.artifactexplorer.common.IdGenerator;

import lombok.RequiredArgsConstructor;

// region/service/RegionService.java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository repo;
    private final IdGenerator idGenerator;

    public List<RegionResponse> findAll() {
        return repo.findAll().stream().map(RegionResponse::from).toList();
    }

    public RegionResponse findById(String id) {
        return repo.findById(id)
                .map(RegionResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Region not found: " + id));
    }

    public List<RegionResponse> search(String name) {
        return repo.findByNameContainingIgnoreCase(name)
                .stream().map(RegionResponse::from).toList();
    }

    public List<RegionResponse> findByModernState(String state) {
        return repo.findByModernState(state)
                .stream().map(RegionResponse::from).toList();
    }

    @Transactional
    public RegionResponse create(RegionRequest req,String creator) {
        Region r = new Region();

        r.setRegionId(idGenerator.generate("REG"));
        r.setName(req.name());
        r.setModernStates(req.modernStates() != null ? req.modernStates() : new HashSet<>());
        r.setCreatedBy(creator);
        return RegionResponse.from(repo.save(r));
    }

    @Transactional
    public RegionResponse update(String id, RegionRequest req) {
        Region r = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Region not found: " + id));
        r.setName(req.name());
        if (req.modernStates() != null)
            r.setModernStates(req.modernStates());
        return RegionResponse.from(repo.save(r));
    }

    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Region not found: " + id);
        repo.deleteById(id);
    }
}