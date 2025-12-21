package com.myorg.billing_backend.controller;

import com.myorg.billing_backend.dto.SubscriptionRequest;
import com.myorg.billing_backend.dto.SubscriptionResponse;
import com.myorg.billing_backend.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping
    public List<SubscriptionResponse> list(@RequestParam Long tenantId) {
        return service.findAllByTenant(tenantId);
    }

    @GetMapping("/{id}")
    public SubscriptionResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(@Valid @RequestBody SubscriptionRequest req) {
        SubscriptionResponse created = service.create(req);
        return ResponseEntity
                .created(URI.create("/api/subscriptions/" + created.getId()))
                .body(created);
    }

    @PostMapping("/{id}/cancel")
    public SubscriptionResponse cancel(@PathVariable Long id,
                                       @RequestParam(defaultValue = "true") boolean immediate) {
        return service.cancel(id, immediate);
    }

    @PostMapping("/{id}/resume")
    public SubscriptionResponse resume(@PathVariable Long id) {
        return service.resume(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
