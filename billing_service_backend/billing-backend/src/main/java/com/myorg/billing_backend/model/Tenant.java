package com.myorg.billing_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tenants")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // maps to BIGSERIAL
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_email")
    private String contactEmail;

    public Tenant() {}

    public Tenant(String name, String contactEmail) {
        this.name = name;
        this.contactEmail = contactEmail;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
}