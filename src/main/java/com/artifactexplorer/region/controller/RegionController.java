package com.artifactexplorer.region.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.artifactexplorer.region.dto.RegionRequest;
import com.artifactexplorer.region.dto.RegionResponse;
import com.artifactexplorer.region.service.RegionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

 // region/controller/RegionController.java
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService service;

    @GetMapping
    public ResponseEntity<List<RegionResponse>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String modernState) {

        if (modernState != null && !modernState.isBlank())
            return ResponseEntity.ok(service.findByModernState(modernState));
        if (name != null && !name.isBlank())
            return ResponseEntity.ok(service.search(name));
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<RegionResponse> create(@Valid @RequestBody RegionRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req, (String) auth.getPrincipal()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionResponse> update(
            @PathVariable String id,
            @Valid @RequestBody RegionRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
} 
