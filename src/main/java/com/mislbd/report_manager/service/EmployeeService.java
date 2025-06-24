package com.mislbd.report_manager.service;


import com.mislbd.report_manager.entity.Employee;
import com.mislbd.report_manager.repository.EmployeeRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public  void saveEmployee(Employee employee){
        employeeRepo.save(employee);
    }

    public Page<Employee> getAllEmployee(Pageable pageable){
     return    employeeRepo.findAll(pageable);
    }
    public List<Employee> getEmployeeByID(Integer id){
        return  employeeRepo.findByEmployeeId(id);
    }
    public List<Employee> getEmployeeByFirstNameOrLastName(String firstName , String lastName){
        return  employeeRepo.findByFirstNameOrLastName(firstName, lastName);
    }

}
