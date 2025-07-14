package com.mislbd.report_manager.controller.customer;

import com.mislbd.report_manager.criteria.CustomerSearchCriteria;
import com.mislbd.report_manager.entity.CustomerEntity;
import com.mislbd.report_manager.service.CustomerService;
import org.springdoc.core.annotations.ParameterObject;
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
    private Page<CustomerEntity> getCustomer(
            Pageable pageable
    ) {
        return  customerService.getAllCustomer(pageable);
    }

    @GetMapping(path = {"searchCustomer"})
    public Page<CustomerEntity> searchCustomers(
            @ParameterObject  Pageable pageable,
            @ParameterObject  CustomerSearchCriteria criteria
    ) {
        return customerService.searchCustomer(criteria,pageable);
    }
}
