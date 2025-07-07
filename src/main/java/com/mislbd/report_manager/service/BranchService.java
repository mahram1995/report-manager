package com.mislbd.report_manager.service;

import com.mislbd.report_manager.entity.BranchEntity;
import com.mislbd.report_manager.repository.BranchRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BranchService {
    private final BranchRepo branchRepo;

    public BranchService(BranchRepo branchRepo) {
        this.branchRepo = branchRepo;
    }

    public Page<BranchEntity> getBranches(Pageable pageable) {

       return   branchRepo.findAll( pageable);
    }
}
