package com.myorg.billing_backend.service;

import com.myorg.billing_backend.model.Invoice;
import com.myorg.billing_backend.repo.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repo;

    public InvoiceService(InvoiceRepository repo) {
        this.repo = repo;
    }

    public List<Invoice> findByTenant(Long tenantId) {
        return repo.findByTenantId(tenantId);
    }

    public Invoice findById(Long tenantId, Long invoiceId) {
        return repo.findById(invoiceId)
                .filter(i -> i.getTenantId().equals(tenantId))
                .orElseThrow(() ->
                        new RuntimeException("Invoice not found for tenant"));
    }
}
