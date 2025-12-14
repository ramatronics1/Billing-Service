package com.myorg.billing_backend.service;

import com.myorg.billing_backend.dto.TenantRequest;
import com.myorg.billing_backend.mapper.TenantMapper;
import com.myorg.billing_backend.model.Tenant;
import com.myorg.billing_backend.repo.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.myorg.billing_backend.exception.ResourceNotFoundException;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TenantService {
    private final TenantRepository repo;
    private final TenantMapper mapper;

    public TenantService(TenantRepository repo, TenantMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<Tenant> findAll() {
        return repo.findAll();
    }

    public Optional<Tenant> findById(Long id) {
        return repo.findById(id);
    }

    @Transactional
    public Tenant create(TenantRequest req) {
        // simple uniqueness check
        if (repo.existsByName(req.getName())) {
            throw new IllegalArgumentException("Tenant with this name already exists");
        }
        Tenant t = mapper.toEntity(req);
        return repo.save(t);
    }

    @Transactional
    public Tenant update(Long id, TenantRequest req) {
        Tenant t = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tenant not found: " + id));
        mapper.updateEntityFromRequest(req, t);
        return repo.save(t);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Tenant not found: " + id);
        repo.deleteById(id);
    }
}
