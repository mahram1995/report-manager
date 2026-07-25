package com.mislbd.report_manager.service.admin;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface QueryService {
    public List<Map<String, Object>> executeQuery(String sql, Map<String, Object> params) ;
}
