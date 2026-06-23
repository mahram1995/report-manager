package com.mislbd.report_manager.command.handler;

import com.mislbd.report_manager.command.CreateBankCommand;
import com.mislbd.report_manager.command.UpdateBankCommand;
import com.mislbd.report_manager.configuration.annotation.CommandAggregate;
import com.mislbd.report_manager.configuration.annotation.CommandHandler;
import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.service.admin.BankService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CommandAggregate
@Component
@RequiredArgsConstructor
public class BankCommandHandler {
    private final BankService service;
    @Transactional
    @CommandHandler
    public CommandResponse<?> createUser(CreateBankCommand command) {
        service.saveFinancialInstitute(command.getPayload());
        return  new CommandResponse<>("Bank Name is :" + command.getPayload().getBankName());
    }

    @Transactional
    @CommandHandler
    public CommandResponse<?> updateUser(UpdateBankCommand command) {
        service.updateFinancialInstitute(command.getPayload());
        return  new CommandResponse<>("Bank Name is :" + command.getPayload().getBankName());
    }
}
