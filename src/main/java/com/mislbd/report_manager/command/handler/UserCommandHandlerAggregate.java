package com.mislbd.report_manager.command.handler;


import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.command.CreateReportGroupCommand;
import com.mislbd.report_manager.command.UpdateReportGroupCommand;
import com.mislbd.report_manager.command.UserModificationCommand;
import com.mislbd.report_manager.configuration.annotation.CommandAggregate;
import com.mislbd.report_manager.configuration.annotation.CommandHandler;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.service.admin.AuthService;
import com.mislbd.report_manager.service.admin.ReportGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CommandAggregate
@Component
@RequiredArgsConstructor
public class UserCommandHandlerAggregate {
    private final AuthService service;
    @Transactional
    @CommandHandler
    public CommandResponse<?> createUser(CreateNewUserCommand command) {
        service.saveUser(command.getPayload());
        return  new CommandResponse<>("User Name is :" + command.getPayload().getUserName());
    }

    @Transactional
    @CommandHandler
    public CommandResponse<?> updateUser(UserModificationCommand command) {
        return  service.updateUser(command.getPayload());
    }

}
