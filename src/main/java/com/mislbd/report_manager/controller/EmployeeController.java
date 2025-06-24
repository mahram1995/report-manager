package com.mislbd.report_manager.controller;

import com.mislbd.report_manager.domain.EmployeeDomain;
import com.mislbd.report_manager.entity.Department;
import com.mislbd.report_manager.entity.Employee;
import com.mislbd.report_manager.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        emp.setDepartment(new Department(101, "Admin"));

        employeeService.saveEmployee(emp);


        return emp;
    }


    @GetMapping(path = "get-employee")
    private Page<Employee> getEmployees(
            Pageable pageable
    ) {
        return employeeService.getAllEmployee(pageable);
    }

    @GetMapping(path = "get-employee/{employeeId}")
    private List<Employee> getEmployee( @PathVariable(name = "employeeId")Integer id
    ) {
        return employeeService.getEmployeeByID(id);
    }

    @GetMapping(path = "get-employee-by-name/{firstName}/{lastName}")
    private List<Employee> getEmployeeByName( @PathVariable  (name = "firstName") String firstName,
                                        @PathVariable  (name = "lastName") String lastName
    ) {
        return employeeService.getEmployeeByFirstNameOrLastName(firstName,lastName);
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
