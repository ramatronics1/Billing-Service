package com.myorg.billing_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

public class CustomerRequest {

    @NotNull(message = "tenantId is required")
    private Long tenantId;

    @Email(message = "must be a valid email")
    private String email;

    private String externalId;

    public CustomerRequest() {}

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
}
