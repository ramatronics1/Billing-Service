package com.myorg.billing_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_cents", nullable = false)
    private Long priceCents = 0L;

    @Column(name = "billing_cycle")
    private String billingCycle;

    @Column(name = "features_json", columnDefinition = "jsonb")
    private String featuresJson;

    @Column(nullable = false)
    private Boolean active = Boolean.TRUE;

    public Plan() {}

    public Plan(Long tenantId, String name, Long priceCents, String billingCycle, String featuresJson, Boolean active) {
        this.tenantId = tenantId;
        this.name = name;
        this.priceCents = priceCents == null ? 0L : priceCents;
        this.billingCycle = billingCycle;
        this.featuresJson = featuresJson;
        this.active = active == null ? Boolean.TRUE : active;
    }

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