package com.mislbd.report_manager.configuration.aopConfig.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.configuration.aopConfig.service.UtilityService;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.sql.rowset.serial.SerialClob;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;

@Component
public class UtilityServiceImpl implements UtilityService {
    private final ObjectMapper objectMapper;

    public UtilityServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    // JSON / Object to CLOB
    @Override
    public Clob jsonToClob(Object data) {
        try {
            if (data == null) {
                return null;
            }

            String json = objectMapper.writeValueAsString(data);

            return new SerialClob(json.toCharArray());

        } catch (Exception e) {
            throw new RuntimeException("JSON to CLOB conversion failed", e);
        }
    }

    // CLOB to Map
    @Override
    public Map<String, Object> clobToMap(Clob clob) {
        try {
            if (clob == null) {
                return new HashMap<>();
            }

            String json = clob.getSubString(1, (int) clob.length());

            return objectMapper.readValue(
                    json,
                    new TypeReference<Map<String, Object>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException("CLOB to Map conversion failed", e);
        }
    }

    // CLOB to JSON String
    public String clobToJson(Clob clob) {
        try {
            if (clob == null) {
                return null;
            }

            return clob.getSubString(1, (int) clob.length());

        } catch (Exception e) {
            throw new RuntimeException("CLOB to JSON conversion failed", e);
        }
    }
}
