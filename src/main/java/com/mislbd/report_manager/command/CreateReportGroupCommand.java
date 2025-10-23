package com.mislbd.report_manager.command;

import com.mislbd.report_manager.configuration.annotation.CommandAttributeTest;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.entity.admin.ReportGroupEntity;
import com.mislbd.report_manager.entity.admin.UserEntity;

@CommandAttributeTest( name = "CREATE_REPORT_GROUP_COMMAND",
        description = "Create Report Group Command",
        module = "Admin")
public class CreateReportGroupCommand extends Command<ReportGroupEntity> {
    public CreateReportGroupCommand(ReportGroupEntity payload){
        super(payload);
    }
}
