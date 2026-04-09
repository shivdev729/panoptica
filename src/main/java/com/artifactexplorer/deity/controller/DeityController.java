package com.artifactexplorer.deity.controller;

import java.util.List;

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

import com.artifactexplorer.deity.dto.DeityRequest;
import com.artifactexplorer.deity.dto.DeityResponse;
import com.artifactexplorer.deity.service.DeityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


// deity/controller/DeityController.java
@RestController
@RequestMapping("/api/deities")
@RequiredArgsConstructor
public class DeityController {

    private final DeityService service;

    @GetMapping
    public ResponseEntity<List<DeityResponse>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String tradition) {

        if (tradition != null && !tradition.isBlank())
            return ResponseEntity.ok(service.findByTradition(tradition));
        if (name != null && !name.isBlank())
            return ResponseEntity.ok(service.search(name));
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeityResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<DeityResponse> create(@Valid @RequestBody DeityRequest req,Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req,(String) auth.getPrincipal()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeityResponse> update(
            @PathVariable String id,
            @Valid @RequestBody DeityRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}