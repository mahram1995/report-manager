package com.mislbd.report_manager.repository;

import com.mislbd.report_manager.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

    ;
}
