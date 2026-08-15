package com.mislbd.report_manager.serviceImpl.admin;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.mislbd.report_manager.domain.admin.ColumnInfoDomain;
import com.mislbd.report_manager.domain.admin.QueryResultDomain;
import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.repository.admin.DatabaseConfigRepository;
import com.mislbd.report_manager.service.admin.QueryService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.sql.Date;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class QueryServiceImpl implements QueryService {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DatabaseConfigRepository databaseConfigRepository;
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public QueryServiceImpl(NamedParameterJdbcTemplate jdbcTemplate, DatabaseConfigRepository databaseConfigRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseConfigRepository = databaseConfigRepository;
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql, Map<String, Object> params) {

        if (sql == null || sql.trim().isEmpty()) {
            throw new RuntimeException("select * from  AIBLFE030426.TF_IMPORT_LC_ISSUE order by transmit_lc_id desc FETCH FIRST 2000 ROWS ONLY");
        }

        String normalizedSql = sql.trim().toLowerCase();

        // Allow only SELECT
        if (!isSelectQuery(sql)) {
            throw new RuntimeException(
                    "Only SELECT queries are permitted"
            );
        }

        if (sql.contains(";")) {
            throw new RuntimeException(
                    "Multiple SQL statements are not allowed"
            );
        }

        DatabaseConfigEntity db = getDatabase(101L)
                .orElseThrow(() -> new RuntimeException("Database not found"));

        NamedParameterJdbcTemplate jdbc = create(db);

        return jdbc.queryForList(sql, params);
    }


    void validatedSql(String sqlString){
        if (!isSelectQuery(sqlString)) {
            throw new RuntimeException("Only SELECT queries are permitted");
        }

        // Prevent multiple SQL statements
        // =========================================================

        if (sqlString.contains(";")) {
            throw new RuntimeException("Multiple SQL statements are not allowed");
        }
    }

    @Override
    public QueryResultDomain executeQueryAdvance(
            String sql,
            Map<String, Object> params,
            Integer isPage,
            Integer page,
            Integer size) {

        DatabaseConfigEntity db = getDataBase();
        NamedParameterJdbcTemplate jdbc = create(db);

        // =========================================================
        // Pagination validation
        // =========================================================

        int pageNumber = (page == null || page < 0) ? 0 : page;
        int pageSize = (size == null || size <= 0) ? 50 : size;


        // IMPORTANT:
        // Calculate offset AFTER pageSize has been assigned
        long offset = (long) pageNumber * pageSize;

        // Variables used inside lambda must be final/effectively final
        final int finalPageNumber = pageNumber;
        final int finalPageSize = pageSize;
        final long finalOffset = offset;

        // Validate SELECT query
        validatedSql(sql);

        String originalSql = sql.trim();
        /*
         * Remove trailing semicolon if necessary.
         * Multiple statements are already rejected above.
         */
        if (originalSql.endsWith(";")) {
            originalSql = originalSql.substring(0, originalSql.length() - 1);
        }


        // Pagination SQL
        String finalQuery;
        if (isPage == 1) {
            finalQuery = "SELECT * FROM (" + originalSql + ") " +
                    "OFFSET " + finalOffset +
                    " ROWS FETCH NEXT " + finalPageSize + " ROWS ONLY";
        } else {
            finalQuery = originalSql;
        }

        // =========================================================
        // Execute paginated query
        // =========================================================

        return jdbc.query(finalQuery, params, rs -> {

                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();

                    // =====================================================
                    // Column metadata
                    // =====================================================
                    List<ColumnInfoDomain> columns =
                            new ArrayList<>();

                    for (int i = 1; i <= columnCount; i++) {
                        ColumnInfoDomain column = new ColumnInfoDomain();
                        column.setName(meta.getColumnLabel(i));
                        column.setSqlType(meta.getColumnTypeName(i));
                        column.setJdbcType(meta.getColumnType(i));
                        columns.add(column);
                    }

                    // =====================================================
                    // Rows
                    // =====================================================

                    List<Map<String, Object>> rows = new ArrayList<>();

                    while (rs.next()) {
                        Map<String, Object> row = new LinkedHashMap<>();

                        for (int i = 1; i <= columnCount; i++) {
                            Object value;
                            int sqlType = meta.getColumnType(i);

                            // =============================================
                            // BLOB
                            // =============================================

                            switch (sqlType) {
                                case Types.BLOB -> {
                                    value = null;
                                }

                                // CLOB
                                case Types.CLOB -> {
                                    Clob clob = rs.getClob(i);
                                    value = clob == null ? null : clob.getSubString(1, (int) clob.length());
                                }

                                // DATE
                                case Types.DATE -> {
                                    var date = rs.getDate(i);
                                    value = date == null ? null : date.toLocalDate().format(DATE_FORMAT);
                                }

                                // TIME
                                case Types.TIME -> {
                                    var time = rs.getTime(i);
                                    value = time == null ? null : time.toLocalTime().format(TIME_FORMAT);
                                }

                                // TIMESTAMP
                                case Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE -> {
                                    Timestamp ts = rs.getTimestamp(i);
                                    value = ts == null ? null : ts.toLocalDateTime().format(DATE_TIME_FORMAT);
                                }

                                // DEFAULT
                                default -> {
                                    value = rs.getObject(i);
                                }
                            }
                            row.put(meta.getColumnLabel(i), value);
                        }
                        rows.add(row);
                    }

                    // =====================================================
                    // Build result
                    // =====================================================
                    QueryResultDomain result = new QueryResultDomain();
                    result.setColumns(columns);
                    result.setRows(rows);
                    result.setPage(finalPageNumber);
                    result.setSize(finalPageSize);
                    return result;
                }
        );
    }

    @Override
    public QueryResultDomain getTotalRecords(String sql, Map<String, Object> params) {
        DatabaseConfigEntity db = getDataBase();
        NamedParameterJdbcTemplate jdbc = create(db);
        String countSql = "SELECT COUNT(*) FROM (" + sql + ")";
        Long totalRows = 0l;
        try {
                totalRows = jdbc.queryForObject(countSql, params, Long.class);

        } catch (BadSqlGrammarException e) {
            throw new RuntimeException(e.getCause());
        }
        QueryResultDomain result = new QueryResultDomain();
        result.setTotalRows(totalRows);
        return  result;
    }

    @Override
    public byte[] exportQueryToExcel(String sql, Map<String, Object> params) {

        DatabaseConfigEntity db = getDatabase(101L).orElseThrow(() -> new RuntimeException("Database not found"));
        NamedParameterJdbcTemplate jdbc = create(db);

        // Validate SELECT
        if (!isSelectQuery(sql)) {
            throw new RuntimeException("Only SELECT queries are permitted");
        }

        // Prevent multiple statements
        if (sql.contains(";")) {
            throw new RuntimeException("Multiple SQL statements are not allowed");
        }

        String originalSql = sql.trim();

        if (originalSql.endsWith(";")) {
            originalSql = originalSql.substring(0, originalSql.length() - 1
            );
        }

        try (
                SXSSFWorkbook workbook = new SXSSFWorkbook(500);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("Data");
            jdbc.query(
                    originalSql,
                    params,
                    rs -> {
                        ResultSetMetaData meta = rs.getMetaData();
                        int columnCount = meta.getColumnCount();

                        // =========================================
                        // Header
                        // =========================================

                        Row headerRow = sheet.createRow(0);

                        for (int i = 1;
                             i <= columnCount;
                             i++) {

                            Cell cell = headerRow.createCell(i - 1);
                            cell.setCellValue(meta.getColumnLabel(i)
                            );
                        }

                        // =========================================
                        // Data
                        // =========================================

                        int rowNumber = 1;
                        while (rs.next()) {
                            Row excelRow = sheet.createRow(rowNumber++);

                            for (int i = 1;
                                 i <= columnCount;
                                 i++) {

                                Object value = getExcelValue(rs, meta, i);
                                Cell cell = excelRow.createCell(i - 1);
                                setCellValue(cell, value);
                            }
                        }
                    }
            );

            workbook.write(outputStream);
            workbook.dispose();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Excel export failed: " + e.getCause().getMessage().toString());
        }
    }

    private Object getExcelValue(
            ResultSet rs,
            ResultSetMetaData meta,
            int index) throws SQLException {

        int sqlType = meta.getColumnType(index);

        switch (sqlType) {
            case Types.BLOB:
                return null;
            case Types.CLOB:
                Clob clob = rs.getClob(index);
                if (clob == null) {
                    return null;
                }

                long length = clob.length();
                if (length > 250) {
                    clob.getSubString(1, 250);
                }

                return clob.getSubString(1, 250);

            case Types.DATE:
                java.sql.Date date = rs.getDate(index);
                return date;
            case Types.TIME:
                Time time = rs.getTime(index);
                return time;
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:

                Timestamp timestamp = rs.getTimestamp(index);
                return timestamp;
            default:
                return rs.getObject(index);
        }
    }

    private void setCellValue(
            Cell cell,
            Object value) {

        if (value == null) {
            cell.setBlank();
            return;
        }

        if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else if (value instanceof Boolean bool) {
            cell.setCellValue(bool);
        } else if (value instanceof java.sql.Date date) {
            cell.setCellValue(date);
        } else if (value instanceof Timestamp timestamp) {
            cell.setCellValue(timestamp);
        } else if (value instanceof Time time) {
            cell.setCellValue(time.toString());
        } else {
            cell.setCellValue(value.toString());
        }
    }

    @Override
    public void executeQueryStream(
            String sql,
            Map<String, Object> params,
            OutputStream outputStream) {


        DatabaseConfigEntity db = getDatabase(101L)
                .orElseThrow(() -> new RuntimeException("Database not found"));

        NamedParameterJdbcTemplate jdbc = create(db);


        if (!isSelectQuery(sql)) {
            throw new RuntimeException("Only SELECT queries are permitted");
        }

        if (sql.contains(";")) {
            throw new RuntimeException("Multiple SQL statements are not allowed");
        }


        jdbc.getJdbcTemplate().setFetchSize(10000);


        jdbc.query(sql, params, rs -> {

            try {

                JsonGenerator json = new JsonFactory()
                        .createGenerator(outputStream);


                ResultSetMetaData meta = rs.getMetaData();

                int columnCount = meta.getColumnCount();


                String[] columnNames = new String[columnCount];
                int[] jdbcTypes = new int[columnCount];


                for (int i = 1; i <= columnCount; i++) {

                    columnNames[i - 1] = meta.getColumnLabel(i);
                    jdbcTypes[i - 1] = meta.getColumnType(i);
                }


                // JSON start
                json.writeStartObject();


                // columns
                json.writeArrayFieldStart("columns");

                for (int i = 0; i < columnCount; i++) {

                    json.writeStartObject();

                    json.writeStringField(
                            "name",
                            columnNames[i]
                    );

                    json.writeStringField(
                            "sqlType",
                            meta.getColumnTypeName(i + 1)
                    );

                    json.writeNumberField(
                            "jdbcType",
                            jdbcTypes[i]
                    );

                    json.writeEndObject();
                }

                json.writeEndArray();


                // rows
                json.writeArrayFieldStart("rows");


                while (rs.next()) {


                    json.writeStartObject();


                    for (int i = 0; i < columnCount; i++) {


                        Object value;


                        switch (jdbcTypes[i]) {


                            case Types.BLOB -> {

                                Blob blob = rs.getBlob(i + 1);

                                if (blob == null) {

                                    value = null;

                                } else {

                                    byte[] bytes =
                                            blob.getBytes(
                                                    1,
                                                    (int) blob.length()
                                            );

                                    value =
                                            Base64.getEncoder()
                                                    .encodeToString(bytes);
                                }
                            }


                            case Types.CLOB -> {

                                Clob clob = rs.getClob(i + 1);

                                value =
                                        clob == null
                                                ? null
                                                : clob.getSubString(
                                                1,
                                                (int) clob.length()
                                        );
                            }


                            case Types.DATE -> {

                                Date date = rs.getDate(i + 1);

                                value =
                                        date == null
                                                ? null
                                                : date.toLocalDate()
                                                .format(DATE_FORMAT);
                            }


                            case Types.TIME -> {

                                Time time = rs.getTime(i + 1);

                                value =
                                        time == null
                                                ? null
                                                : time.toLocalTime()
                                                .format(TIME_FORMAT);
                            }


                            case Types.TIMESTAMP,
                                    Types.TIMESTAMP_WITH_TIMEZONE -> {


                                Timestamp ts =
                                        rs.getTimestamp(i + 1);


                                value =
                                        ts == null
                                                ? null
                                                : ts.toLocalDateTime()
                                                .format(DATE_TIME_FORMAT);
                            }


                            default -> {

                                value = rs.getObject(i + 1);
                            }

                        }


                        if (value == null) {

                            json.writeNullField(columnNames[i]);

                        } else {

                            json.writeObjectField(
                                    columnNames[i],
                                    value
                            );
                        }

                    }


                    json.writeEndObject();


                    // send data immediately
                    json.flush();
                }


                json.writeEndArray();

                json.writeEndObject();


                json.flush();


            } catch (Exception e) {

                throw new RuntimeException(e);
            }

            return null;
        });
    }

    private boolean isSelectQuery(String sql) {

        String query = sql
                .replaceAll("--.*", "")
                .replaceAll("/\\*.*?\\*/", "")
                .trim()
                .toLowerCase();

        return query.startsWith("select")
                || query.startsWith("with");
    }


    public Optional<DatabaseConfigEntity> getDatabase(Long id) {
        return databaseConfigRepository.findById(id);


    }


    public NamedParameterJdbcTemplate create(DatabaseConfigEntity db) {

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName(db.getDriverClass());
        ds.setUrl(db.getUrl());
        ds.setUsername(db.getUsername());
        ds.setPassword(db.getPassword());

        return new NamedParameterJdbcTemplate(ds);
    }

    DatabaseConfigEntity getDataBase(){
        return getDatabase(101L).orElseThrow(() -> new RuntimeException("Database not found"));
    };
}
