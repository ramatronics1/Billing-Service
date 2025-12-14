package com.myorg.billing_backend.repo;

import com.myorg.billing_backend.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByTenantId(Long tenantId);
    Optional<Plan> findByTenantIdAndName(Long tenantId, String name);
    boolean existsByTenantIdAndName(Long tenantId, String name);
}
