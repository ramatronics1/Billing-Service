package com.myorg.billing_backend.mapper;

import com.myorg.billing_backend.dto.SubscriptionRequest;
import com.myorg.billing_backend.dto.SubscriptionResponse;
import com.myorg.billing_backend.model.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public Subscription toEntity(SubscriptionRequest req) {
        return new Subscription(
                req.getTenantId(),
                req.getCustomerId(),
                req.getPlanId(),
                req.getStartDate(),
                req.getEndDate(),
                null, // status will be set in service
                req.getAutoRenew(),
                req.getMetadata()
        );
    }

    public void updateEntityFromRequest(SubscriptionRequest req, Subscription s) {
        if (req.getTenantId() != null) s.setTenantId(req.getTenantId());
        if (req.getCustomerId() != null) s.setCustomerId(req.getCustomerId());
        if (req.getPlanId() != null) s.setPlanId(req.getPlanId());
        if (req.getStartDate() != null) s.setStartDate(req.getStartDate());
        if (req.getEndDate() != null) s.setEndDate(req.getEndDate());
        if (req.getAutoRenew() != null) s.setAutoRenew(req.getAutoRenew());
        if (req.getMetadata() != null) s.setMetadata(req.getMetadata());
    }

    public SubscriptionResponse toResponse(Subscription s) {
        SubscriptionResponse r = new SubscriptionResponse();
        r.setId(s.getId());
        r.setTenantId(s.getTenantId());
        r.setCustomerId(s.getCustomerId());
        r.setPlanId(s.getPlanId());
        r.setStartDate(s.getStartDate());
        r.setEndDate(s.getEndDate());
        r.setStatus(s.getStatus());
        r.setAutoRenew(s.getAutoRenew());
        r.setMetadata(s.getMetadata());
        return r;
    }
}
