package com.myorg.billing_backend.dto;

import java.time.OffsetDateTime;

public class CustomerResponse {

    private Long id;
    private Long tenantId;
    private String email;
    private String externalId;

    public CustomerResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
}
