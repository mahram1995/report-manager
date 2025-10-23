package com.mislbd.report_manager.command.handler;

import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.command.CreateReportGroupCommand;
import com.mislbd.report_manager.command.UpdateReportGroupCommand;
import com.mislbd.report_manager.configuration.annotation.CommandAggregate;
import com.mislbd.report_manager.configuration.annotation.CommandHandler;
import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.service.admin.ReportGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@CommandAggregate
@Component
public class ReportGroupCommandHandlerAggregate {
    private final ReportGroupService service;
    @Transactional
    @CommandHandler
    public CommandResponse<?> createReportGroup(CreateReportGroupCommand command) {
        return  service.saveReportGroup(command.getPayload());
    }

    @Transactional
    @CommandHandler
    public CommandResponse<?> updateReportGroup(UpdateReportGroupCommand command) {
        return  service.updateReportGroup(command.getPayload());
    }



}
