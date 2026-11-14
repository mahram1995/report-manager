package com.mislbd.report_manager.controller.admin;

import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.service.admin.JasperDataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/database-configuration")
@RequiredArgsConstructor
public class DatabaseConfigController {
    private final JasperDataSourceService databaseConfigService;

    @PostMapping("/create-database")
    public ResponseEntity<String> createDataSource(
            @RequestBody DatabaseConfigEntity data) {

        return databaseConfigService.createDataSource(data);
    }

    @PutMapping("/update-database")
    public ResponseEntity<String> updateDataSource(
            @RequestBody DatabaseConfigEntity data) {

        return databaseConfigService.updateDataSource(data);
    }

    @PostMapping("/test-connection")
    public ResponseEntity<String> testConnection(
            @RequestBody DatabaseConfigEntity data) {

        return databaseConfigService.testConnection(data);
    }

    @GetMapping("/get-database-list")
    public ResponseEntity<List<DatabaseConfigEntity>> getDatabaseList() {

        return ResponseEntity.ok(
                databaseConfigService.getDatabaseList()
        );
    }
}
