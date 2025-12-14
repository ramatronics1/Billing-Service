package com.myorg.billing_backend.mapper;

import com.myorg.billing_backend.dto.TenantRequest;
import com.myorg.billing_backend.dto.TenantResponse;
import com.myorg.billing_backend.model.Tenant;
import org.springframework.stereotype.Component;

@Component
public class TenantMapper {
    public Tenant toEntity(TenantRequest req) {
        return new Tenant(req.getName(), req.getContactEmail());
    }

    public TenantResponse toResponse(Tenant t) {
        return new TenantResponse(t.getId(), t.getName(), t.getContactEmail());
    }

    public void updateEntityFromRequest(TenantRequest req, Tenant t) {
        if (req.getName() != null) t.setName(req.getName());
        if (req.getContactEmail() != null) t.setContactEmail(req.getContactEmail());
    }
}