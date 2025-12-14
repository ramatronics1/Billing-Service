package com.myorg.billing_backend.controller;

import com.myorg.billing_backend.dto.PlanRequest;
import com.myorg.billing_backend.dto.PlanResponse;
import com.myorg.billing_backend.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService service;

    public PlanController(PlanService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> listByTenant(@RequestParam Long tenantId) {
        List<PlanResponse> list = service.findAllByTenant(tenantId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PlanResponse> create(@Valid @RequestBody PlanRequest req) {
        PlanResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/plans/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanResponse> update(@PathVariable Long id, @Valid @RequestBody PlanRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
