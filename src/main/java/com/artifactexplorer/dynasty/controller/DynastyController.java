package com.artifactexplorer.dynasty.controller;

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
import org.springframework.web.bind.annotation.CrossOrigin;

import com.artifactexplorer.dynasty.dto.DynastyEntryResponse;
import com.artifactexplorer.dynasty.dto.DynastyRequest;
import com.artifactexplorer.dynasty.dto.DynastyResponse;
import com.artifactexplorer.dynasty.service.DynastyService;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

// dynasty/controller/DynastyController.java
@RestController
@CrossOrigin
@RequestMapping("/api/dynasties")
@RequiredArgsConstructor
public class DynastyController {

    private final DynastyService service;

    @GetMapping
    public ResponseEntity<List<DynastyResponse>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer periodStart,
            @RequestParam(required = false) Integer periodEnd) {

        if (periodStart != null && periodEnd != null)
            return ResponseEntity.ok(service.findByPeriod(periodStart, periodEnd));
        if (name != null && !name.isBlank())
            return ResponseEntity.ok(service.search(name));
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DynastyEntryResponse> get(@PathVariable String id) {
        DynastyResponse response = service.findById(id);
        List<String> ruledStates = service.getRuledStates(id);
        return ResponseEntity.ok(new DynastyEntryResponse(
            response.dynastyId(), response
            .name(), response.periodStart(), response.periodEnd(),
            response.createdAt(), response.updatedAt(), ruledStates
        ));
    }


    @PostMapping
    public ResponseEntity<DynastyResponse> create(@Valid @RequestBody DynastyRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req,(String) auth.getPrincipal()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DynastyResponse> update(
            @PathVariable String id,
            @Valid @RequestBody DynastyRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
