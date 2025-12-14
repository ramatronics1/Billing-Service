package com.myorg.billing_backend.controller;

import com.myorg.billing_backend.dto.TenantRequest;
import com.myorg.billing_backend.dto.TenantResponse;
import com.myorg.billing_backend.mapper.TenantMapper;
import com.myorg.billing_backend.model.Tenant;
import com.myorg.billing_backend.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {
    private final TenantService service;
    private final TenantMapper mapper;

    public TenantController(TenantService service, TenantMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<TenantResponse> list() {
        List<Tenant> tenants = service.findAll();
        return tenants.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> get(@PathVariable Long id) {
        return service.findById(id)
                .map(t -> ResponseEntity.ok(mapper.toResponse(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TenantResponse> create(@Valid @RequestBody TenantRequest req) {
        Tenant created = service.create(req);
        TenantResponse resp = mapper.toResponse(created);
        return ResponseEntity.created(URI.create("/api/tenants/" + resp.getId())).body(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> update(@PathVariable Long id, @Valid @RequestBody TenantRequest req) {
        Tenant updated = service.update(id, req);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}