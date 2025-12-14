package com.myorg.billing_backend.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // maps to BIGSERIAL
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "email")
    private String email;

    @Column(name = "external_id")
    private String externalId;

    public Customer() {}

    public Customer(Long tenantId, String email, String externalId) {
        this.tenantId = tenantId;
        this.email = email;
        this.externalId = externalId;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTenantId() { return tenantId; }
    public void setTenantId(Long tenantId) { this.tenantId = tenantId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
}