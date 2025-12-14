package com.myorg.billing_backend.mapper;

import com.myorg.billing_backend.dto.CustomerRequest;
import com.myorg.billing_backend.dto.CustomerResponse;
import com.myorg.billing_backend.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest req) {
        return new Customer(req.getTenantId(), req.getEmail(), req.getExternalId());
    }

    public void updateEntityFromRequest(CustomerRequest req, Customer c) {
        // update allowed fields (tenantId typically shouldn't change but we allow it if provided)
        if (req.getTenantId() != null) c.setTenantId(req.getTenantId());
        if (req.getEmail() != null) c.setEmail(req.getEmail());
        if (req.getExternalId() != null) c.setExternalId(req.getExternalId());
    }

    public CustomerResponse toResponse(Customer c) {
        CustomerResponse r = new CustomerResponse();
        r.setId(c.getId());
        r.setTenantId(c.getTenantId());
        r.setEmail(c.getEmail());
        r.setExternalId(c.getExternalId());
        return r;
    }
}
