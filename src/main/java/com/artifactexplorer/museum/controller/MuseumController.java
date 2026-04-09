package com.artifactexplorer.museum.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.artifactexplorer.museum.service.MuseumService;
import com.artifactexplorer.museum.dto.MuseumRequest;
import com.artifactexplorer.museum.dto.MuseumResponse;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;

// museum/controller/MuseumController.java
@RestController
@RequestMapping("/api/museums")
@RequiredArgsConstructor
public class MuseumController {

    private final MuseumService service;

    @GetMapping
    public ResponseEntity<List<MuseumResponse>> list(
            @RequestParam(required = false) String name) {
        List<MuseumResponse> result = (name != null && !name.isBlank())
                ? service.search(name)
                : service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MuseumResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<MuseumResponse> create(@Valid @RequestBody MuseumRequest req,Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req, (String) auth.getPrincipal()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MuseumResponse> update(
            @PathVariable String id,
            @Valid @RequestBody MuseumRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}