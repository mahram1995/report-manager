package com.mislbd.report_manager.service;

import com.mislbd.report_manager.criteria.CustomerSearchCriteria;
import com.mislbd.report_manager.entity.CustomerEntity;
import com.mislbd.report_manager.repository.CustomerRepo;
import com.mislbd.report_manager.specification.CustomerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Page<CustomerEntity> getAllCustomer(Pageable pageable) {
        return customerRepo.findAll(pageable);

    }

    public Page<CustomerEntity> searchCustomer(CustomerSearchCriteria criteria, Pageable pageable) {

        Specification<CustomerEntity> spec = CustomerSpecification.getCustomerSpecification(criteria);
        return customerRepo.findAll(spec,pageable);
    }
}
