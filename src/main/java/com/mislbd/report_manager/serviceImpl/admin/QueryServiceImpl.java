package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.domain.admin.ColumnInfoDomain;
import com.mislbd.report_manager.domain.admin.QueryResultDomain;
import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.repository.admin.DatabaseConfigRepository;
import com.mislbd.report_manager.service.admin.QueryService;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.ZoneId;
import java.util.*;

@Component
public class QueryServiceImpl implements QueryService {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DatabaseConfigRepository databaseConfigRepository;

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

    @Override
    public QueryResultDomain executeQueryAdvance(String sql, Map<String, Object> params) {

        DatabaseConfigEntity db = getDatabase(101L)
                .orElseThrow(() -> new RuntimeException("Database not found"));

        NamedParameterJdbcTemplate jdbc = create(db);

        // Allow only SELECT
        if (!isSelectQuery(sql)) {
            throw new RuntimeException("Only SELECT queries are permitted");
        }

        // Prevent multiple statements
        if (sql.contains(";")) {
            throw new RuntimeException("Multiple SQL statements are not allowed");
        }

        return jdbc.query(sql, params, rs -> {

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            List<ColumnInfoDomain> columns = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                ColumnInfoDomain column = new ColumnInfoDomain();
                column.setName(meta.getColumnLabel(i));
                column.setSqlType(meta.getColumnTypeName(i));
                column.setJdbcType(meta.getColumnType(i));
                columns.add(column);
            }

            List<Map<String, Object>> rows = new ArrayList<>();

            while (rs.next()) {

                Map<String, Object> row = new LinkedHashMap<>();

                for (int i = 1; i <= columnCount; i++) {

                    Object value;
                    int sqlType = meta.getColumnType(i);

                    switch (sqlType) {

                        case Types.BLOB -> {
                            Blob blob = rs.getBlob(i);
                            if (blob == null) {
                                value = null;
                            } else {
                                byte[] bytes = blob.getBytes(1, (int) blob.length());

                                // Option 1: Base64
                                value = Base64.getEncoder().encodeToString(bytes);

                                // Option 2:
                                // value = bytes;
                            }
                        }

                        case Types.CLOB -> {
                            Clob clob = rs.getClob(i);
                            value = (clob == null)
                                    ? null
                                    : clob.getSubString(1, (int) clob.length());
                        }

                        case Types.DATE -> {
                            var date = rs.getDate(i);
                            value = date == null ? null : date.toLocalDate();
                        }

                        case Types.TIME -> {
                            var time = rs.getTime(i);
                            value = time == null ? null : time.toLocalTime();
                        }

                        case Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE -> {
                            Timestamp ts = rs.getTimestamp(i);
                            value = ts == null
                                    ? null
                                    : ts.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
                        }

                        default -> {

                            Object obj = rs.getObject(i);

                            if (obj instanceof InputStream inputStream) {

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                try {
                                    inputStream.transferTo(baos);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                value = Base64.getEncoder()
                                        .encodeToString(baos.toByteArray());

                            } else {
                                value = obj;
                            }
                        }
                    }

                    row.put(meta.getColumnLabel(i), value);
                }

                rows.add(row);
            }

            QueryResultDomain result = new QueryResultDomain();
            result.setColumns(columns);
            result.setRows(rows);

            return result;
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
        return  databaseConfigRepository.findById(id);


    }


    public NamedParameterJdbcTemplate create(DatabaseConfigEntity db) {

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName(db.getDriverClass());
        ds.setUrl(db.getUrl());
        ds.setUsername(db.getUsername());
        ds.setPassword(db.getPassword());

        return new NamedParameterJdbcTemplate(ds);
    }
}
