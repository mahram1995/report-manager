package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.domain.admin.QueryResultDomain;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Service
public interface QueryService {
    public List<Map<String, Object>> executeQuery(String sql, Map<String, Object> params) ;
    public QueryResultDomain executeQueryAdvance(String sql, Map<String, Object> params, Integer isPage, Integer page, Integer size) ;
    public QueryResultDomain getTotalRecords(String sql, Map<String, Object> params) ;
    public byte[] exportQueryToExcel(String sql, Map<String, Object> params);
    void executeQueryStream(
            String sql,
            Map<String, Object> params,
            OutputStream outputStream
    );
}
