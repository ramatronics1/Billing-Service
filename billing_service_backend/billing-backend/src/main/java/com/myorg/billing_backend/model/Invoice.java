package com.myorg.billing_backend.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "pdf_path")
    private String pdfPath;

    @Column(name = "amount_cents")
    private Long amountCents;

    @Column(name = "currency")
    private String currency;

    @Column(name = "issued_at")
    private OffsetDateTime issuedAt;

    @Column(name = "due_at")
    private OffsetDateTime dueAt;

    @Column(name = "status")
    private String status; // open, paid, failed

    public Invoice() {}

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public String getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(String invoiceNo) { this.invoiceNo = invoiceNo; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }

    public Long getAmountCents() { return amountCents; }
    public void setAmountCents(Long amountCents) { this.amountCents = amountCents; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public OffsetDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(OffsetDateTime issuedAt) { this.issuedAt = issuedAt; }

    public OffsetDateTime getDueAt() { return dueAt; }
    public void setDueAt(OffsetDateTime dueAt) { this.dueAt = dueAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
