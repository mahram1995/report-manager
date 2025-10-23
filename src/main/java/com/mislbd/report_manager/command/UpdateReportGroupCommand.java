package com.mislbd.report_manager.command;

import com.mislbd.report_manager.configuration.annotation.CommandAttributeTest;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.entity.admin.ReportGroupEntity;

@CommandAttributeTest( name = "UPDATE_REPORT_GROUP_COMMAND",
        description = "Update Report Group Command",
        module = "Admin")
public class UpdateReportGroupCommand  extends Command<ReportGroupEntity> {
    public UpdateReportGroupCommand(ReportGroupEntity payload){
        super(payload);
    }
}