package com.mislbd.report_manager.domain.admin;

import lombok.Data;

@Data
public class ColumnInfoDomain {
    private String name;
    private String sqlType;
    private int jdbcType;
}
