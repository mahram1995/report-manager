package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.repository.admin.DatabaseConfigRepository;
import com.mislbd.report_manager.service.admin.QueryService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
