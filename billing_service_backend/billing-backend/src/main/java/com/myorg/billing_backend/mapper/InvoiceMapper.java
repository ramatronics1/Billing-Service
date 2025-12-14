package com.myorg.billing_backend.mapper;

import com.myorg.billing_backend.dto.InvoiceResponse;
import com.myorg.billing_backend.model.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceResponse toResponse(Invoice i) {
        return new InvoiceResponse(
                i.getId(),
                i.getInvoiceNo(),
                i.getAmountCents(),
                i.getCurrency(),
                i.getStatus(),
                i.getIssuedAt(),
                i.getDueAt()
        );
    }
}
