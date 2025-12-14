package com.myorg.billing_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class PlanRequest {

    @NotNull(message = "tenantId is required")
    private Long tenantId;

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "priceCents is required")
    @Min(value = 0, message = "priceCents must be >= 0")
    private Long priceCents;

    private String billingCycle; // e.g. "monthly", "yearly"

    /**
     * JSON string for features (persisted as jsonb). Keep as String for now.
     * Example: {"users":10,"storage_gb":50}
     */
    private String featuresJson;

    private Boolean active;

    public PlanRequest() {}

    // getters & setters
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getPriceCents() { return priceCents; }
    public void setPriceCents(Long priceCents) { this.priceCents = priceCents; }

    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }

    public String getFeaturesJson() { return featuresJson; }
    public void setFeaturesJson(String featuresJson) { this.featuresJson = featuresJson; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
