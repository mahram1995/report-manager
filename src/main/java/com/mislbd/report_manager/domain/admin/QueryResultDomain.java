package com.mislbd.report_manager.domain.admin;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryResultDomain {
    private List<ColumnInfoDomain> columns;
    private List<Map<String, Object>> rows;
    private long totalRows;

    private int page;

    private int size;

    private int totalPages;
}
