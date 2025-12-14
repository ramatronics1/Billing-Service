package com.myorg.billing_backend.repo;

import com.myorg.billing_backend.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByTenantId(Long tenantId);
    List<Subscription> findByCustomerId(Long customerId);
}
