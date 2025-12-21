package com.myorg.billing_backend.repo;

import com.myorg.billing_backend.model.Subscription;
import com.myorg.billing_backend.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByTenantId(Long tenantId);
    List<Subscription> findByStatusAndAutoRenewFalseAndEndDateBefore(
            SubscriptionStatus status,
            LocalDate endDate
    );
}

