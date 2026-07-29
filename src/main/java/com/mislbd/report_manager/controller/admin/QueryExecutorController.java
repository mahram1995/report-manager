package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.domain.admin.QueryResultDomain;
import com.mislbd.report_manager.service.admin.QueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/query-executor")
public class QueryExecutorController {

    private final QueryService queryService;

    public QueryExecutorController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping("/execute")
    public ResponseEntity<List<Map<String, Object>>> executeQuery(@RequestBody QueryRequest request) {
        List<Map<String, Object>> result = queryService.executeQuery(
                request.getSql(),
                request.getParams()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/execute-with-column-type")
    public QueryResultDomain executeQueryWithDataType(@RequestBody QueryRequest request) {
        QueryResultDomain  result = queryService.executeQueryWithDataType(
                request.getSql(),
                request.getParams()
        );
        return ResponseEntity.ok(result).getBody();
    }
}


// DTO to hold the incoming request
class QueryRequest {
    private String sql;
    private Map<String, Object> params;

    // Getters and Setters
    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }
    public Map<String, Object> getParams() { return params; }
    public void setParams(Map<String, Object> params) { this.params = params; }
}
