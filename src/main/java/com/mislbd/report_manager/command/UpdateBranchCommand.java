package com.mislbd.report_manager.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.HasIdentity;
import com.mislbd.report_manager.entity.admin.BranchEntity;
import com.mislbd.report_manager.entity.admin.UserEntity;

@CommandAttribute( name = "UPDATE_BRANCH_COMMAND",
        description = "Update Branch Command",
        module = "Admin")
public class UpdateBranchCommand extends Command<BranchEntity> implements HasIdentity {
    @JsonCreator
    public UpdateBranchCommand(@JsonProperty("payload") BranchEntity payload){
        super(payload);
    }

    @Override
    public String getIdentity() {
        return getPayload().getName();
    }
}
