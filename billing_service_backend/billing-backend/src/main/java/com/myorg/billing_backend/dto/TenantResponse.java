package com.myorg.billing_backend.dto;

public class TenantResponse {
    private Long id;
    private String name;
    private String contactEmail;

    public TenantResponse() {}
    public TenantResponse(Long id, String name, String contactEmail) {
        this.id = id; this.name = name; this.contactEmail = contactEmail;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getContactEmail() { return contactEmail; }
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
}
