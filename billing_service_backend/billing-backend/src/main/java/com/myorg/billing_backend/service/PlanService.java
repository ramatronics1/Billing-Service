package com.myorg.billing_backend.service;

import com.myorg.billing_backend.dto.PlanRequest;
import com.myorg.billing_backend.dto.PlanResponse;
import com.myorg.billing_backend.exception.ResourceNotFoundException;
import com.myorg.billing_backend.mapper.PlanMapper;
import com.myorg.billing_backend.model.Plan;
import com.myorg.billing_backend.repo.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    private final PlanRepository repo;
    private final PlanMapper mapper;

    public PlanService(PlanRepository repo, PlanMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<PlanResponse> findAllByTenant(Long tenantId) {
        List<Plan> list = repo.findByTenantId(tenantId);
        return list.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public PlanResponse findById(Long id) {
        Plan p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + id));
        return mapper.toResponse(p);
    }

    @Transactional
    public PlanResponse create(PlanRequest req) {
        // simple uniqueness check per tenant
        if (repo.existsByTenantIdAndName(req.getTenantId(), req.getName())) {
            throw new IllegalArgumentException("Plan with this name already exists for the tenant");
        }
        Plan entity = mapper.toEntity(req);
        Plan saved = repo.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public PlanResponse update(Long id, PlanRequest req) {
        Plan p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + id));
        mapper.updateEntityFromRequest(req, p);
        Plan saved = repo.save(p);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Plan not found: " + id);
        repo.deleteById(id);
    }
}
