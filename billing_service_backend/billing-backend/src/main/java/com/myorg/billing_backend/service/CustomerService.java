package com.myorg.billing_backend.service;

import com.myorg.billing_backend.dto.CustomerRequest;
import com.myorg.billing_backend.dto.CustomerResponse;
import com.myorg.billing_backend.exception.ResourceNotFoundException;
import com.myorg.billing_backend.mapper.CustomerMapper;
import com.myorg.billing_backend.model.Customer;
import com.myorg.billing_backend.repo.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository repo;
    private final CustomerMapper mapper;

    public CustomerService(CustomerRepository repo, CustomerMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<CustomerResponse> findAllByTenant(Long tenantId) {
        List<Customer> list = repo.findByTenantId(tenantId);
        return list.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public CustomerResponse findById(Long id) {
        Customer c = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        return mapper.toResponse(c);
    }

    @Transactional
    public CustomerResponse create(CustomerRequest req) {
        if (req.getExternalId() != null && repo.existsByTenantIdAndExternalId(req.getTenantId(), req.getExternalId())) {
            throw new IllegalArgumentException("Customer with externalId already exists for this tenant");
        }
        Customer entity = mapper.toEntity(req);
        Customer saved = repo.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerRequest req) {
        Customer c = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        mapper.updateEntityFromRequest(req, c);
        Customer saved = repo.save(c);
        return mapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Customer not found: " + id);
        repo.deleteById(id);
    }
}