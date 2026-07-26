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

        DatabaseConfigEntity db = getDatabase(101L)
                .orElseThrow(() -> new RuntimeException("Database not found"));

        NamedParameterJdbcTemplate jdbc = create(db);
        String query="select * from  AIBLFE030426.TF_IMPORT_LC_ISSUE order by transmit_lc_id desc FETCH FIRST 2000 ROWS ONLY";
         return jdbc.queryForList(query, params);//  jdbcTemplate.queryForList(sqls, params);
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
