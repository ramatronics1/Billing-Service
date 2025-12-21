package com.myorg.billing_backend.service;

import com.myorg.billing_backend.dto.SubscriptionRequest;
import com.myorg.billing_backend.dto.SubscriptionResponse;
import com.myorg.billing_backend.exception.ResourceNotFoundException;
import com.myorg.billing_backend.mapper.SubscriptionMapper;
import com.myorg.billing_backend.model.*;
import com.myorg.billing_backend.repo.CustomerRepository;
import com.myorg.billing_backend.repo.PlanRepository;
import com.myorg.billing_backend.repo.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repo;
    private final CustomerRepository customerRepo;
    private final PlanRepository planRepo;
    private final SubscriptionMapper mapper;

    public SubscriptionService(SubscriptionRepository repo,
                               CustomerRepository customerRepo,
                               PlanRepository planRepo,
                               SubscriptionMapper mapper) {
        this.repo = repo;
        this.customerRepo = customerRepo;
        this.planRepo = planRepo;
        this.mapper = mapper;
    }

    public SubscriptionResponse findById(Long id) {
        Subscription s = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + id));
        return mapper.toResponse(s);
    }

    public List<SubscriptionResponse> findAllByTenant(Long tenantId) {
        return repo.findByTenantId(tenantId)
                .stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public SubscriptionResponse create(SubscriptionRequest req) {

        Customer customer = customerRepo.findById(req.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Plan plan = planRepo.findById(req.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        Subscription s = mapper.toEntity(req);

        LocalDate today = LocalDate.now();
        s.setStartDate(s.getStartDate() == null ? today : s.getStartDate());

        if (s.getStartDate().isAfter(today)) {
            s.setStatus(SubscriptionStatus.SCHEDULED);
        } else {
            s.setStatus(SubscriptionStatus.ACTIVE);
        }

        if (s.getAutoRenew() == null) {
            s.setAutoRenew(true);
        }

        return mapper.toResponse(repo.save(s));
    }

    @Transactional
    public SubscriptionResponse cancel(Long id, boolean immediate) {
        Subscription s = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        s.setAutoRenew(false);

        if (immediate) {
            s.setStatus(SubscriptionStatus.CANCELED);
            s.setEndDate(LocalDate.now());
        }

        return mapper.toResponse(repo.save(s));
    }

    @Transactional
    public SubscriptionResponse resume(Long id) {
        Subscription s = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        if (s.getStatus() != SubscriptionStatus.PAUSED) {
            throw new IllegalStateException("Only paused subscriptions can be resumed");
        }

        s.setStatus(SubscriptionStatus.ACTIVE);
        s.setAutoRenew(true);

        return mapper.toResponse(repo.save(s));
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Subscription not found");
        }
        repo.deleteById(id);
    }
}
