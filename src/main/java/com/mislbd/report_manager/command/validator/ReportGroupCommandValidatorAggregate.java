package com.mislbd.report_manager.command.validator;


import com.mislbd.report_manager.command.CreateNewUserCommand;
import com.mislbd.report_manager.command.CreateReportGroupCommand;
import com.mislbd.report_manager.command.UpdateReportGroupCommand;
import com.mislbd.report_manager.configuration.annotation.CommandAggregate;
import com.mislbd.report_manager.configuration.annotation.CommandHandler;
import com.mislbd.report_manager.configuration.annotation.CommandValidator;
import com.mislbd.report_manager.configuration.annotation.CommandValidatorAggregate;
import com.mislbd.report_manager.configuration.aopConfig.domain.CommandResponse;
import com.mislbd.report_manager.entity.admin.ReportGroupEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@CommandValidatorAggregate
@Component
public class ReportGroupCommandValidatorAggregate {

    @CommandValidator
    public boolean ValidateCreateReportGroupCommand(CreateReportGroupCommand command) {
        ReportGroupEntity entity=command.getPayload();
        if(entity.getGroupName()==null){
            throw new RuntimeException("Group Name Can't Be Null");
        }
        return  true;
    }

    @CommandValidator
    public boolean ValidateUpdateReportGroupCommand(UpdateReportGroupCommand command) {
        ReportGroupEntity entity=command.getPayload();
        if(entity.getGroupName()==null){
            throw new RuntimeException("Group Name Can't Be Null");
        }
        return  true;
    }
}
