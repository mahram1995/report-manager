package com.mislbd.report_manager.controller;

import com.mislbd.report_manager.criteria.CustomerSearchCriteria;
import com.mislbd.report_manager.entity.CustomerEntity;
import com.mislbd.report_manager.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            CustomerSearchCriteria criteria
    ) {


        return customerService.searchCustomer(criteria,pageable);
    }
}
