package com.myorg.billing_backend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class SubscriptionRequest {

    @NotNull(message = "tenantId is required")
    private Long tenantId;

    @NotNull(message = "customerId is required")
    private Long customerId;

    @NotNull(message = "planId is required")
    private Long planId;

    // optional - default to today if null
    private LocalDate startDate;

    // optional
    private LocalDate endDate;

    private Boolean autoRenew;

    // JSON string
    private String metadata;

    public SubscriptionRequest() {}

    // getters & setters
    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Boolean getAutoRenew() { return autoRenew; }
    public void setAutoRenew(Boolean autoRenew) { this.autoRenew = autoRenew; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
}
