package com.mislbd.report_manager.repository;

import com.mislbd.report_manager.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepo extends JpaRepository<CustomerEntity, Long> , JpaSpecificationExecutor<CustomerEntity> {
}
