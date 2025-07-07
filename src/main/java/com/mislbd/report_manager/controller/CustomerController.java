package com.mislbd.report_manager.controller;

import com.mislbd.report_manager.entity.CustomerEntity;
import com.mislbd.report_manager.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin/customer")
public class CustomerController {
    private  final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = "get-customer")
    private Page<CustomerEntity> getBranches(
            Pageable pageable
    ) {
        return  customerService.getAllCustomer(pageable);
    }

    @GetMapping("/search")
    public Page<CustomerEntity> searchCustomers(
            Pageable pageable,
            @RequestParam(required = false) Long cusId,
            @RequestParam(required = false) String isActive,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) String customerType,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String mobileNumber,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long divisionId,
            @RequestParam(required = false) Long postCodeId,
            @RequestParam(required = false) Long upazillaId,
            @RequestParam(required = false) String isNidReceived,
            @RequestParam(required = false) String isSuspended,
            @RequestParam(required = false) String isVerified,
            @RequestParam(required = false) String isNegativeListedCustomer
    ) {


        return customerService.searchCustomer(cusId,isActive,
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
                isNegativeListedCustomer,pageable);
    }
}
