package com.mislbd.report_manager.command.validator;
import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.command.UserModificationCommand;
import com.mislbd.report_manager.configuration.annotation.CommandValidator;
import com.mislbd.report_manager.configuration.annotation.CommandValidatorAggregate;
import com.mislbd.report_manager.exception.CommandValidationException;
import com.mislbd.report_manager.repository.admin.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@RequiredArgsConstructor
@CommandValidatorAggregate
@Component
public class UserCommandValidatorAggregate {

    private  final UserRepository repository;
    @CommandValidator
    public boolean ValidateCreateUserGroupCommand(CreateNewUserCommand command) {
        if(repository.existsByUserName(command.getPayload().getUserName())){
            throw new CommandValidationException("Username already exists. Please try another name.");

        }
        return  true;
    }

    @CommandValidator
    public boolean ValidateUpdateUserCommand(UserModificationCommand command) {
        return  true;
    }
}
