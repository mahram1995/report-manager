package com.mislbd.report_manager.configuration.aopConfig.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public interface HasIdentity extends Serializable {
    @JsonIgnore
    String getIdentity();
}
