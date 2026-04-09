package com.artifactexplorer.deity.service;

import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.artifactexplorer.deity.dto.DeityRequest;
import com.artifactexplorer.deity.dto.DeityResponse;
import com.artifactexplorer.deity.entity.Deity;
import com.artifactexplorer.deity.repository.DeityRepository;
import com.artifactexplorer.common.IdGenerator;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import lombok.RequiredArgsConstructor;

// deity/service/DeityService.java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeityService {

    private final DeityRepository repo;
    private final IdGenerator idGenerator;

    public List<DeityResponse> findAll() {
        return repo.findAll().stream().map(DeityResponse::from).toList();
    }

    public DeityResponse findById(String id) {
        return repo.findById(id)
                .map(DeityResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Deity not found: " + id));
    }

    public List<DeityResponse> search(String name) {
        return repo.findByNameContainingIgnoreCase(name)
                .stream().map(DeityResponse::from).toList();
    }

    public List<DeityResponse> findByTradition(String tradition) {
        return repo.findByTradition(tradition)
                .stream().map(DeityResponse::from).toList();
    }

    @Transactional
    public DeityResponse create(DeityRequest req,String creator) {
        Deity d = new Deity();
        d.setDeityId(idGenerator.generate("DEI"));
        d.setName(req.name());
        d.setTraditions(req.traditions() != null ? req.traditions() : new HashSet<>());
        d.setCreatedBy(creator);
        return DeityResponse.from(repo.save(d));
    }

    @Transactional
    public DeityResponse update(String id, DeityRequest req) {
        Deity d = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Deity not found: " + id));
        d.setName(req.name());
        if (req.traditions() != null) d.setTraditions(req.traditions());
        return DeityResponse.from(repo.save(d));
    }

    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Deity not found: " + id);
        repo.deleteById(id);
    }
}
