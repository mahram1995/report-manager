package com.mislbd.report_manager.controller.admin;


import com.mislbd.report_manager.command.CreateBranchCommand;
import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.command.UpdateBranchCommand;
import com.mislbd.report_manager.command.UserModificationCommand;
import com.mislbd.report_manager.configuration.aopConfig.service.CommandProcessor;
import com.mislbd.report_manager.domain.admin.BranchDomain;
import com.mislbd.report_manager.entity.admin.BranchEntity;
import com.mislbd.report_manager.entity.admin.UserEntity;
import com.mislbd.report_manager.service.admin.BranchService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/financial-institute")
public class BranchController {
   private final CommandProcessor commandProcessor;
    private final BranchService branchService;

    public BranchController(CommandProcessor commandProcessor, BranchService branchService) {
        this.commandProcessor = commandProcessor;
        this.branchService = branchService;
    }

    @PostMapping("/create-branch")
    public ResponseEntity<?> createBranch(@RequestBody BranchEntity data) {
        return ResponseEntity.ok(commandProcessor.executeCommand(new CreateBranchCommand(data)));
    }

    @PutMapping("/update-branch")
    public ResponseEntity<?> updateBranch(@RequestBody BranchEntity data) {
        return ResponseEntity.ok(commandProcessor.executeCommand(new UpdateBranchCommand(data)));
    }

    @GetMapping(path = "get-branch/{branchId}")
    private BranchDomain getBranchById(@PathVariable ("branchId") int branchId) {
        return branchService.getBranchById(branchId);
    }

    @GetMapping(path = "get-branch")
    private Object getBranches(
          @ParameterObject Pageable pageable,
          @RequestParam(name = "branchName", required = false) String branchName,
          @RequestParam(name = "bankId", required = false) String bankId,
          @RequestParam(name = "branchId", required = false) String branchId,

          @RequestParam(name = "asPage", defaultValue = "true") boolean asPage
    ) {
        return branchService.getBranch(branchName, bankId,branchId,asPage,pageable);
    }



}
