package com.mislbd.report_manager.service;

import com.mislbd.report_manager.entity.Employee;
import com.mislbd.report_manager.repository.EmployeeRepo;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Long saveEmployee(Employee employee){
        Employee emp= employeeRepo.save(employee);
        return emp.getEmployeeId();
    };

}
