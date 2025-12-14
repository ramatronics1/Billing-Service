package com.myorg.billing_backend.repo;

import com.myorg.billing_backend.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByTenantId(Long tenantId);

    List<Invoice> findByTenantIdAndStatus(Long tenantId, String status);
}
