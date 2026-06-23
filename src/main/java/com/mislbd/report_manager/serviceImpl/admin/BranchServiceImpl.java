package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.domain.admin.BranchDomain;
import com.mislbd.report_manager.entity.admin.BranchEntity;
import com.mislbd.report_manager.mapper.admin.BranchMapper;
import com.mislbd.report_manager.repository.admin.BranchRepo;
import com.mislbd.report_manager.service.admin.BranchService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class BranchServiceImpl implements BranchService {
    private  final BranchRepo branchRepo;

    public BranchServiceImpl(BranchRepo branchRepo) {
        this.branchRepo = branchRepo;
    }

    @Override
    public CommandResponse<?> saveBranch(BranchEntity data) {
        return new CommandResponse<>(branchRepo.save(data));
    }

    @Override
    public CommandResponse<?> updateBranch(BranchEntity data) {
        return new CommandResponse<>(branchRepo.save(data));
    }

    @Override
    public Object getBranch(String branchName,
                            String bankId,
                            String branchId,
                            boolean asPage,
                            Pageable pageable) {

        branchName = "%" + (branchName == null ? "" : branchName.trim()) + "%";
       // bankId = (bankId == null ? null : bankId.trim());
       // branchId = (bankId == null ? null : branchId.trim());

        if (asPage) {
            return branchRepo.findBranches(branchName, bankId,branchId, pageable)
                    .map(BranchMapper::EntityToDomain);
        }

        return branchRepo.findBranches(branchName, bankId,branchId)
                .stream()
                .map(BranchMapper::EntityToDomain)
                .toList();
    }

    @Override
    public BranchDomain getBranchById(int branchId) {
        return branchRepo.findById(branchId).map(BranchMapper::EntityToDomain)
                .orElse(null);

    }
}
