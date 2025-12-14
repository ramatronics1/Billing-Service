package com.myorg.billing_backend.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payment_attempts")
public class PaymentAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber = 1;

    @Column(name = "status", nullable = false)
    private String status; // pending, success, failed

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "last_attempt_at")
    private OffsetDateTime lastAttemptAt;

    @Column(name = "next_attempt_at")
    private OffsetDateTime nextAttemptAt;

    public PaymentAttempt() {}

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Integer getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(Integer attemptNumber) { this.attemptNumber = attemptNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getLastAttemptAt() { return lastAttemptAt; }
    public void setLastAttemptAt(OffsetDateTime lastAttemptAt) { this.lastAttemptAt = lastAttemptAt; }

    public OffsetDateTime getNextAttemptAt() { return nextAttemptAt; }
    public void setNextAttemptAt(OffsetDateTime nextAttemptAt) { this.nextAttemptAt = nextAttemptAt; }
}
