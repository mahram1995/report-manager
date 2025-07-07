package com.mislbd.report_manager.service;

import com.mislbd.report_manager.entity.CustomerEntity;
import com.mislbd.report_manager.repository.CustomerRepo;
import com.mislbd.report_manager.specification.CustomerSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Page<CustomerEntity> getAllCustomer(Pageable pageable){
        return customerRepo.findAll(pageable);

    }

    public Page<CustomerEntity> searchCustomer( Long cusId,String isActive,
                                               Long branchId,
                                               String customerType,
                                               String gender,
                                               String mobileNumber,
                                               Long countryId,
                                               Long districtId,
                                               Long divisionId,
                                               Long postCodeId,
                                               Long upazillaId,
                                               String isNidReceived,
                                               String isSuspended,
                                               String isVerified,
                                               String isNegativeListedCustomer, Pageable pageable){


        return customerRepo.findAll((Pageable) CustomerSpecification.searchCustomer(cusId,
                isActive,
                branchId,
                customerType,
                gender,
                mobileNumber,
                countryId,
                districtId,
                divisionId,
                postCodeId,
                upazillaId,
                isNidReceived,
                isSuspended,
                isVerified,
                isNegativeListedCustomer));

    }
}
