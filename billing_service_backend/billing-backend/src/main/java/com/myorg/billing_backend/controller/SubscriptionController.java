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
    public ResponseEntity<List<SubscriptionResponse>> listByTenant(@RequestParam Long tenantId) {
        return ResponseEntity.ok(service.findAllByTenant(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(@Valid @RequestBody SubscriptionRequest req) {
        SubscriptionResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/subscriptions/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> update(@PathVariable Long id, @Valid @RequestBody SubscriptionRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    /**
     * Cancel subscription.
     * Query param immediate=true/false (default true)
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponse> cancel(@PathVariable Long id,
                                                       @RequestParam(defaultValue = "true") boolean immediate) {
        return ResponseEntity.ok(service.cancel(id, immediate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
