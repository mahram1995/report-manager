package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.domain.admin.ColumnInfoDomain;
import com.mislbd.report_manager.domain.admin.QueryResultDomain;
import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.repository.admin.DatabaseConfigRepository;
import com.mislbd.report_manager.service.admin.QueryService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSetMetaData;
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
            sql="";
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

    public QueryResultDomain executeQueryWithDataType(String sql, Map<String, Object> params) {
        DatabaseConfigEntity db = getDatabase(101L)
                .orElseThrow(() -> new RuntimeException("Database not found"));
        NamedParameterJdbcTemplate jdbc = create(db);

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

        return jdbc.query(sql, params, rs -> {

            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();

            List<ColumnInfoDomain> columns = new ArrayList<>();

            for (int i = 1; i <= count; i++) {
                ColumnInfoDomain c = new ColumnInfoDomain();
                c.setName(meta.getColumnLabel(i));
                c.setSqlType(meta.getColumnTypeName(i));   // VARCHAR, DECIMAL, DATETIME...
                c.setJdbcType(meta.getColumnType(i));      // java.sql.Types.INTEGER...
                columns.add(c);
            }

            List<Map<String, Object>> rows = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();

                for (int i = 1; i <= count; i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
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
