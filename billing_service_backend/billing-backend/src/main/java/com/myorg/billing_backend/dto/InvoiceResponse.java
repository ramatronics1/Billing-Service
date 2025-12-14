package com.myorg.billing_backend.dto;

import java.time.Instant;
import java.time.OffsetDateTime;

public class InvoiceResponse {

    private Long id;
    private String invoiceNo;
    private Long amountCents;
    private String currency;
    private String status;
    private OffsetDateTime issuedAt;
    private OffsetDateTime dueAt;

    public InvoiceResponse() {}

    public InvoiceResponse(
            Long id,
            String invoiceNo,
            Long amountCents,
            String currency,
            String status,
            OffsetDateTime issuedAt,
            OffsetDateTime dueAt
    ) {
        this.id = id;
        this.invoiceNo = invoiceNo;
        this.amountCents = amountCents;
        this.currency = currency;
        this.status = status;
        this.issuedAt = issuedAt;
        this.dueAt = dueAt;
    }

    public Long getId() { return id; }
    public String getInvoiceNo() { return invoiceNo; }
    public Long getAmountCents() { return amountCents; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
    public OffsetDateTime getIssuedAt() { return issuedAt; }
    public OffsetDateTime getDueAt() { return dueAt; }
}
