package com.mislbd.report_manager.repository;

import com.mislbd.report_manager.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    List<Employee> findByEmployeeId(Integer id);

    List<Employee> findByFirstNameOrLastName(String firstName, String lastName);

    ;
}
