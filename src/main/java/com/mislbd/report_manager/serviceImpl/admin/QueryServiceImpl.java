package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.service.admin.QueryService;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class QueryServiceImpl implements QueryService {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public QueryServiceImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql, Map<String, Object> params) {
        String sqls="select * from BUDGET_TRANSACTION_demo ";
         return jdbcTemplate.queryForList(sqls, params);
    }
}
