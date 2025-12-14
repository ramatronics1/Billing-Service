package com.myorg.billing_backend.repo;

import com.myorg.billing_backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByTenantId(Long tenantId);
    Optional<Customer> findByTenantIdAndExternalId(Long tenantId, String externalId);
    boolean existsByTenantIdAndExternalId(Long tenantId, String externalId);
}
