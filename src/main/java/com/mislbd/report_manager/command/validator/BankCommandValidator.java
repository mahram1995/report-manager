package com.mislbd.report_manager.command.validator;

import com.mislbd.report_manager.command.CreateBankCommand;
import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.command.UpdateBankCommand;
import com.mislbd.report_manager.command.UserModificationCommand;
import com.mislbd.report_manager.configuration.annotation.CommandValidator;
import com.mislbd.report_manager.configuration.annotation.CommandValidatorAggregate;
import com.mislbd.report_manager.domain.admin.BankDomain;
import com.mislbd.report_manager.entity.admin.BankEntity;
import com.mislbd.report_manager.exception.CommandValidationException;
import com.mislbd.report_manager.repository.admin.BankRepository;
import com.mislbd.report_manager.repository.admin.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@CommandValidatorAggregate
@Component
public class BankCommandValidator {
    private  final BankRepository repository;
    @CommandValidator
    public boolean ValidateCreateUserGroupCommand(CreateBankCommand command) {
        BankDomain bank=command.getPayload();
        if(repository.existsByBankName(command.getPayload().getBankName())){
            throw new CommandValidationException("Bank Name Already Exists. Please try with another name.");
        }
        if (bank.getSwiftCode() == null || bank.getSwiftCode().isBlank()) {
           // throw new CommandValidationException("Swift code can't be null or blank");
        }
        return  true;
    }

    @CommandValidator
    public boolean ValidateUpdateUserCommand(UpdateBankCommand command) {
        return  true;
    }
}
