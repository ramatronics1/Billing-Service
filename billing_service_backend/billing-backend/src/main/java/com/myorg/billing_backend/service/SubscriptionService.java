package com.myorg.billing_backend.service;

import com.myorg.billing_backend.dto.SubscriptionRequest;
import com.myorg.billing_backend.dto.SubscriptionResponse;
import com.myorg.billing_backend.exception.ResourceNotFoundException;
import com.myorg.billing_backend.mapper.SubscriptionMapper;
import com.myorg.billing_backend.model.Subscription;
import com.myorg.billing_backend.repo.CustomerRepository;
import com.myorg.billing_backend.repo.PlanRepository;
import com.myorg.billing_backend.repo.SubscriptionRepository;
import com.myorg.billing_backend.model.Customer;
import com.myorg.billing_backend.model.Plan;
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
        // Validate customer exists and belongs to tenant
        Customer customer = customerRepo.findById(req.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + req.getCustomerId()));
        if (!customer.getTenantId().equals(req.getTenantId())) {
            throw new IllegalArgumentException("Customer does not belong to tenant: " + req.getTenantId());
        }

        // Validate plan exists and belongs to tenant
        Plan plan = planRepo.findById(req.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + req.getPlanId()));
        if (!plan.getTenantId().equals(req.getTenantId())) {
            throw new IllegalArgumentException("Plan does not belong to tenant: " + req.getTenantId());
        }

        // Build entity
        Subscription s = mapper.toEntity(req);

        // Default start date & status logic
        LocalDate today = LocalDate.now();
        if (s.getStartDate() == null) {
            s.setStartDate(today);
            s.setStatus("active");
        } else {
            if (s.getStartDate().isAfter(today)) {
                s.setStatus("scheduled");
            } else {
                s.setStatus("active");
            }
        }

        // default autoRenew if null
        if (s.getAutoRenew() == null) s.setAutoRenew(Boolean.TRUE);

        Subscription saved = repo.save(s);
        return mapper.toResponse(saved);
    }

    @Transactional
    public SubscriptionResponse update(Long id, SubscriptionRequest req) {
        Subscription s = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + id));

        // If updating plan or customer, validate tenant consistency
        if (req.getCustomerId() != null && !req.getCustomerId().equals(s.getCustomerId())) {
            var customer = customerRepo.findById(req.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + req.getCustomerId()));
            if (!customer.getTenantId().equals(s.getTenantId())) {
                throw new IllegalArgumentException("Customer does not belong to subscription tenant");
            }
        }

        if (req.getPlanId() != null && !req.getPlanId().equals(s.getPlanId())) {
            var plan = planRepo.findById(req.getPlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + req.getPlanId()));
            if (!plan.getTenantId().equals(s.getTenantId())) {
                throw new IllegalArgumentException("Plan does not belong to subscription tenant");
            }
        }

        mapper.updateEntityFromRequest(req, s);

        // If startDate moved to future, set scheduled
        LocalDate today = LocalDate.now();
        if (s.getStartDate() != null && s.getStartDate().isAfter(today)) {
            s.setStatus("scheduled");
        }

        Subscription saved = repo.save(s);
        return mapper.toResponse(saved);
    }

    /**
     * Cancel subscription.
     * If immediate==true: mark canceled and set endDate = today.
     * If immediate==false: set autoRenew = false (subscription remains active until period end).
     */
    @Transactional
    public SubscriptionResponse cancel(Long id, boolean immediate) {
        Subscription s = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + id));

        if (immediate) {
            s.setStatus("canceled");
            s.setEndDate(LocalDate.now());
            s.setAutoRenew(Boolean.FALSE);
        } else {
            // mark not to auto renew; actual endDate will be computed by billing job later
            s.setAutoRenew(Boolean.FALSE);
        }

        Subscription saved = repo.save(s);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Subscription not found: " + id);
        repo.deleteById(id);
    }
}
