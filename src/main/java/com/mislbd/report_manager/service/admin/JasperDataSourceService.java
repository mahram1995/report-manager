package com.mislbd.report_manager.service.admin;

import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JasperDataSourceService {

    ResponseEntity<String> createDataSource(DatabaseConfigEntity data);
    ResponseEntity<String> updateDataSource(DatabaseConfigEntity data);
    ResponseEntity<String> testConnection(DatabaseConfigEntity data);

    List<DatabaseConfigEntity> getDatabaseList();


}
