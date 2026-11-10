package com.mislbd.report_manager.configuration.aopConfig.service;

import org.springframework.stereotype.Service;

import java.sql.Clob;
import java.util.Map;

@Service
public interface UtilityService {
    public Clob jsonToClob(Object data);
    public Map<String, Object> clobToMap(Clob clob);
    public String clobToJson(Clob clob);
}
