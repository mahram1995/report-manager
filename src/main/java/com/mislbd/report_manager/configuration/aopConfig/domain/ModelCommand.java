package com.mislbd.report_manager.configuration.aopConfig.domain;

import com.mislbd.report_manager.entity.admin.UserEntity;

public class ModelCommand extends Command<Object>{
    public ModelCommand(Object payload){
        super(payload);
    }
}
