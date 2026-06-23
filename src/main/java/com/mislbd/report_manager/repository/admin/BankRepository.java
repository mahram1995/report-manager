package com.mislbd.report_manager.repository.admin;

import com.mislbd.report_manager.entity.admin.BankEntity;
import com.mislbd.report_manager.entity.admin.BranchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {
    List<BankEntity> findByBankNameContainingIgnoreCase(String branchName);

    Page<BankEntity> findByBankNameContainingIgnoreCase(
            String branchName,
            Pageable pageable);

    boolean existsByBankName(String bankName);
}
