package com.mislbd.report_manager.service;


import com.mislbd.report_manager.entity.Employee;
import com.mislbd.report_manager.repository.EmployeeRepo;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public  void saveEmployee(Employee employee){
        employeeRepo.save(employee);
    }
}
