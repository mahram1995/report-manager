package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.domain.admin.BranchDomain;
import com.mislbd.report_manager.entity.admin.BranchEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BranchService {
    public CommandResponse<?> saveBranch(BranchEntity data);
    public CommandResponse<?> updateBranch(BranchEntity data);
    public Object  getBranch(String branchName, String bankId, String branchId, boolean asPage, Pageable pageable);
    public BranchDomain getBranchById(int branchId);
}
