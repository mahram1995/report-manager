package com.mislbd.report_manager.controller;

import com.mislbd.report_manager.domain.EmployeeDomain;
import com.mislbd.report_manager.entity.Department;
import com.mislbd.report_manager.entity.Employee;
import com.mislbd.report_manager.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping(path = "/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping(path = "save-employee")
    public Employee saveEmployee() {

        Employee emp = new Employee();

        emp.setFirstName("Mahram");
        emp.setEmail("mahram@mislbd.com");
        emp.setPhone("01710298374");
        emp.setAddress("Dhaka, Mirpur, Bangladesh");
        emp.setDepartment(new Department(101L, "Admin"));

        employeeService.saveEmployee(emp);


        return emp;
    }


    private List<EmployeeDomain> getEmployees(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id,asc") String sort
    ) {

        return List.of(

        );
    }

    @GetMapping("/{employeeId}")
    private ResponseEntity<EmployeeDomain> getEmployeeById(@PathVariable(name = "employeeId") Long empId) {

        EmployeeDomain employee = new EmployeeDomain();

        if (Objects.isNull(employee)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }


}
