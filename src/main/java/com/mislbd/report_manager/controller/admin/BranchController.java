package com.mislbd.report_manager.controller.admin;


import com.mislbd.report_manager.entity.BranchEntity;
import com.mislbd.report_manager.service.BranchService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin/branch")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping(path = "get-branch")
    private Page<BranchEntity> getBranches(
          @ParameterObject Pageable pageable
    ) {
        return  branchService.getBranches(pageable);
    }

}
