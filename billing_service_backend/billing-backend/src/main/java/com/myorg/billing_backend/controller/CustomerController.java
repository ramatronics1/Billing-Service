package com.myorg.billing_backend.controller;

import com.myorg.billing_backend.dto.CustomerRequest;
import com.myorg.billing_backend.dto.CustomerResponse;
import com.myorg.billing_backend.mapper.CustomerMapper;
import com.myorg.billing_backend.model.Customer;
import com.myorg.billing_backend.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listByTenant(@RequestParam Long tenantId) {
        List<CustomerResponse> list = service.findAllByTenant(tenantId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest req) {
        CustomerResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/customers/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long id, @Valid @RequestBody CustomerRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
