package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.domain.admin.QueryResultDomain;
import com.mislbd.report_manager.service.admin.QueryService;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.HashMap;
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
    public QueryResultDomain executeQueryAdvance(@RequestBody QueryRequest request) {

        return queryService.executeQueryAdvance(
                request.getSql(),
                request.getParams(),
                request.getAsPage(),
                request.getPage(),
                request.getSize()
        );
    }

    @PostMapping("/get-total-records")
    public QueryResultDomain getTotalRecords(@RequestBody QueryRequest request) {
        return queryService.getTotalRecords(request.getSql(), request.getParams());
    }

    @PostMapping("/execute-stream")
    public StreamingResponseBody executeStream(
            @RequestBody QueryRequest request) {


        return outputStream -> {

            queryService.executeQueryStream(
                    request.getSql(),
                    new HashMap<>(),
                    outputStream
            );

        };
    }

    @PostMapping(value = "/export-excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportExcel(@RequestBody QueryRequest request) {
        byte[] excelFile =
                queryService.exportQueryToExcel(
                        request.getSql(),
                        request.getParams()
                );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"query-result.xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelFile.length)
                .body(excelFile);
    }
}


// DTO to hold the incoming request
@Data
class QueryRequest {
    private String sql;
    private Map<String, Object> params;

    // 0-based page number
    private Integer page = 0;
    private Integer asPage=1 ;


    // Default 50 rows per page
    private Integer size = 50;
}


