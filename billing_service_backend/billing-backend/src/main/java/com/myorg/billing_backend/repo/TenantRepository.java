package com.myorg.billing_backend.repo;

import com.myorg.billing_backend.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    // Optional helpers
    Optional<Tenant> findByName(String name);
    boolean existsByName(String name);
}