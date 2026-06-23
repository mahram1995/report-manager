package com.mislbd.report_manager.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.HasIdentity;
import com.mislbd.report_manager.entity.admin.BranchEntity;
import com.mislbd.report_manager.entity.admin.UserEntity;

@CommandAttribute( name = "CREATE_BRANCH_COMMAND",
        description = "Crate Branch Command",
        module = "Admin")
public class CreateBranchCommand extends Command<BranchEntity> implements HasIdentity {
    @JsonCreator
    public CreateBranchCommand(@JsonProperty("payload") BranchEntity payload){
        super(payload);
    }

    @Override
    public String getIdentity() {
        return getPayload().getName();
    }
}
