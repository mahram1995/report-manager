package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.criteria.CustomerSearchCriteria;
import com.mislbd.report_manager.entity.CustomerEntity;
import com.mislbd.report_manager.service.admin.CustomerService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("admin/customer")
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
    public ResponseEntity<?> searchCustomers(
            @ParameterObject  Pageable pageable,
            @ParameterObject  CustomerSearchCriteria criteria,
            @RequestParam(name = "asPage", defaultValue = "true") boolean asPage
    ) {
        if(asPage){
            return ResponseEntity.ok(customerService.searchCustomer(criteria,pageable)) ;
        }else{
            return ResponseEntity.ok(customerService.searchCustomer(criteria));
        }

    }
}
