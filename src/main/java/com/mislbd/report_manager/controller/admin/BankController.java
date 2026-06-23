package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.command.CreateBankCommand;
import com.mislbd.report_manager.command.UpdateBankCommand;
import com.mislbd.report_manager.configuration.aopConfig.service.CommandProcessor;
import com.mislbd.report_manager.domain.admin.BankDomain;
import com.mislbd.report_manager.entity.admin.BankEntity;
import com.mislbd.report_manager.service.admin.BankService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/financial-institute")
public class BankController {
    private  final CommandProcessor commandProcessor ;
    private final BankService bankService;

    public BankController(CommandProcessor commandProcessor, BankService bankService) {
        this.commandProcessor = commandProcessor;
        this.bankService = bankService;
    }

    @PostMapping("/create-financial-institute")
    public ResponseEntity<?> createFinancialInstitute(@RequestBody BankDomain data) {
        return ResponseEntity.ok(commandProcessor.executeCommand(new CreateBankCommand(data)));
    }

    @PutMapping("/update-financial-institute")
    public ResponseEntity<?> updateFinancialInstitute(@RequestBody BankDomain data) {
        return ResponseEntity.ok(commandProcessor.executeCommand(new UpdateBankCommand(data)));
    }

    @GetMapping(path = "get-financial-institute/{bankId}")
    public BankDomain getFinancialInstituteById(
            @PathVariable("bankId") Long bankId) {

        return bankService.getFinancialInstituteById(bankId);
    }
    @GetMapping(path = "get-financial-institute")
    private Object getFinancialInstitute(
            @ParameterObject Pageable pageable,
            @RequestParam(name = "bankName", required = false) String bankName,
            @RequestParam(name = "asPage", defaultValue = "false") boolean asPage
    ) {
        return bankService.getFinancialInstitute(bankName,asPage,pageable);
    }
}
