package com.myorg.billing_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TenantRequest {
    @NotBlank(message = "name is required")
    private String name;

    @Email(message = "must be a valid email")
    private String contactEmail;

    public TenantRequest() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
}
