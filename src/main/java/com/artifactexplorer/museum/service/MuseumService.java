package com.artifactexplorer.museum.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.artifactexplorer.museum.entity.Museum;
import com.artifactexplorer.museum.repository.MuseumRepository;
import com.artifactexplorer.museum.dto.MuseumRequest;
import com.artifactexplorer.museum.dto.MuseumResponse;
import jakarta.persistence.EntityNotFoundException;
import com.artifactexplorer.common.IdGenerator;

// museum/service/MuseumService.java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MuseumService {

    private final MuseumRepository repo;
    private final IdGenerator idGenerator;

    public List<MuseumResponse> findAll() {
        return repo.findAll().stream().map(MuseumResponse::from).toList();
    }

    public MuseumResponse findById(String id) {
        return repo.findById(id)
                .map(MuseumResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Museum not found: " + id));
    }

    public List<MuseumResponse> search(String name) {
        return repo.findByNameContainingIgnoreCase(name)
                .stream().map(MuseumResponse::from).toList();
    }

    @Transactional
    public MuseumResponse create(MuseumRequest req, String adminId) {
        Museum m = new Museum();
        m.setMuseumId(idGenerator.generate("MUS"));
        m.setName(req.name());
        m.setLocation(req.location());
        return MuseumResponse.from(repo.save(m));
    }

    @Transactional
    public MuseumResponse update(String id, MuseumRequest req) {
        Museum m = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Museum not found: " + id));
        m.setName(req.name());
        m.setLocation(req.location());
        return MuseumResponse.from(repo.save(m));
    }

    @Transactional
    public void delete(String id) {
        if (!repo.existsById(id))
            throw new EntityNotFoundException("Museum not found: " + id);
        repo.deleteById(id);
    }
}