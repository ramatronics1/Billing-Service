package com.myorg.billing_backend.service;

import com.myorg.billing_backend.model.Invoice;
import com.myorg.billing_backend.repo.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repo;
    private final InvoicePdfService pdfService;

    public InvoiceService(InvoiceRepository repo ,  InvoicePdfService pdfService) {
        this.repo = repo;
        this.pdfService = pdfService;
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

    @Transactional
    public Invoice generatePdf(Long tenantId, Long invoiceId) {
        Invoice invoice = findById(tenantId, invoiceId);

        if (invoice.getPdfPath() != null) {
            return invoice; // idempotent
        }

        String path = pdfService.generatePdf(invoice);
        invoice.setPdfPath(path);

        return repo.save(invoice);
    }

}
