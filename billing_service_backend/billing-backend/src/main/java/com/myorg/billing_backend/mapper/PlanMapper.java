package com.myorg.billing_backend.mapper;

import com.myorg.billing_backend.dto.PlanRequest;
import com.myorg.billing_backend.dto.PlanResponse;
import com.myorg.billing_backend.model.Plan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {

    public Plan toEntity(PlanRequest req) {
        return new Plan(
                req.getTenantId(),
                req.getName(),
                req.getPriceCents(),
                req.getBillingCycle(),
                req.getFeaturesJson(),
                req.getActive() == null ? Boolean.TRUE : req.getActive()
        );
    }

    public void updateEntityFromRequest(PlanRequest req, Plan p) {
        if (req.getTenantId() != null) p.setTenantId(req.getTenantId());
        if (req.getName() != null) p.setName(req.getName());
        if (req.getPriceCents() != null) p.setPriceCents(req.getPriceCents());
        if (req.getBillingCycle() != null) p.setBillingCycle(req.getBillingCycle());
        if (req.getFeaturesJson() != null) p.setFeaturesJson(req.getFeaturesJson());
        if (req.getActive() != null) p.setActive(req.getActive());
    }

    public PlanResponse toResponse(Plan p) {
        PlanResponse r = new PlanResponse();
        r.setId(p.getId());
        r.setTenantId(p.getTenantId());
        r.setName(p.getName());
        r.setPriceCents(p.getPriceCents());
        r.setBillingCycle(p.getBillingCycle());
        r.setFeaturesJson(p.getFeaturesJson());
        r.setActive(p.getActive());
        return r;
    }
}
