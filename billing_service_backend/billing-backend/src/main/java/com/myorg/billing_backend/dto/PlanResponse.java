package com.myorg.billing_backend.dto;

public class PlanResponse {

    private Long id;
    private Long tenantId;
    private String name;
    private Long priceCents;
    private String billingCycle;
    private String featuresJson;
    private Boolean active;

    public PlanResponse() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
