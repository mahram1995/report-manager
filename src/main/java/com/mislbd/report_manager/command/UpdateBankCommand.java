package com.mislbd.report_manager.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mislbd.report_manager.configuration.annotation.CommandAttribute;
import com.mislbd.report_manager.configuration.aopConfig.domain.Command;
import com.mislbd.report_manager.configuration.aopConfig.domain.HasIdentity;
import com.mislbd.report_manager.domain.admin.BankDomain;
import com.mislbd.report_manager.entity.admin.BankEntity;
import com.mislbd.report_manager.entity.admin.BranchEntity;
import com.mislbd.report_manager.entity.admin.UserEntity;

@CommandAttribute( name = "UPDATE_BANK_COMMAND",
        description = "Update Bank Command",
        module = "Admin")
public class UpdateBankCommand extends Command<BankDomain> implements HasIdentity {
    @JsonCreator
    public UpdateBankCommand(@JsonProperty("payload") BankDomain payload){
        super(payload);
    }

    @Override
    public String getIdentity() {
        return getPayload().getBankName();
    }
}
