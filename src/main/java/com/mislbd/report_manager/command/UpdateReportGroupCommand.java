package com.mislbd.report_manager.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.HasIdentity;
import com.mislbd.report_manager.entity.admin.ReportGroupEntity;

@CommandAttribute( name = "UPDATE_REPORT_GROUP_COMMAND",
        description = "Update Report Group Command",
        module = "Admin")
public class UpdateReportGroupCommand  extends Command<ReportGroupEntity>  implements HasIdentity {
    @JsonCreator
    public UpdateReportGroupCommand(@JsonProperty("payload")  ReportGroupEntity payload){
        super(payload);
    }

    @Override
    public String getIdentity() {
        return getPayload().getGroupName();
    }
}