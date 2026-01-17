package com.myorg.billing_backend.controller;

import com.myorg.billing_backend.dto.InvoiceResponse;
import com.myorg.billing_backend.mapper.InvoiceMapper;
import com.myorg.billing_backend.model.Invoice;
import com.myorg.billing_backend.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService service;
    private final InvoiceMapper mapper;

    public InvoiceController(InvoiceService service, InvoiceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // GET /api/invoices?tenantId=1
    @GetMapping
    public List<InvoiceResponse> list(@RequestParam Long tenantId) {
        return service.findByTenant(tenantId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // GET /api/invoices/{id}?tenantId=1
    @GetMapping("/{id}")
    public InvoiceResponse get(
            @PathVariable Long id,
            @RequestParam Long tenantId
    ) {
        Invoice invoice = service.findById(tenantId, id);
        return mapper.toResponse(invoice);
    }

    @PostMapping("/{id}/pdf")
    public InvoiceResponse generatePdf(
            @PathVariable Long id,
            @RequestParam Long tenantId
    ) {
        Invoice invoice = service.generatePdf(tenantId, id);
        return mapper.toResponse(invoice);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long id,
            @RequestParam Long tenantId
    ) throws Exception {

        Invoice invoice = service.findById(tenantId, id);

        if (invoice.getPdfPath() == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Path.of(invoice.getPdfPath());
        byte[] pdfBytes = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header(
                        "Content-Disposition",
                        "attachment; filename=\"" + path.getFileName() + "\""
                )
                .body(pdfBytes);
    }

}
