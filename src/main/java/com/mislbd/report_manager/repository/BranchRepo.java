package com.mislbd.report_manager.repository;

import com.mislbd.report_manager.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepo extends JpaRepository<BranchEntity, Integer> {
}
