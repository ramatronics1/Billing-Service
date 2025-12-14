package com.myorg.billing_backend.repo;

import com.myorg.billing_backend.model.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface PaymentAttemptRepository extends JpaRepository<PaymentAttempt, Long> {
    List<PaymentAttempt> findByNextAttemptAtBeforeAndStatus(OffsetDateTime ts, String status);
    List<PaymentAttempt> findByInvoiceIdOrderByAttemptNumberDesc(Long invoiceId);
}
