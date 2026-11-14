package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.repository.admin.DatabaseConfigRepository;
import com.mislbd.report_manager.service.admin.JasperDataSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Component
public class JasperDataSourceServiceImpl implements JasperDataSourceService {
    private final DatabaseConfigRepository databaseConfigRepository;

    public JasperDataSourceServiceImpl(DatabaseConfigRepository databaseConfigRepository) {
        this.databaseConfigRepository = databaseConfigRepository;
    }

    @Override
    public ResponseEntity<String> createDataSource(DatabaseConfigEntity data) {

        try {

            databaseConfigRepository.save(data);

            return ResponseEntity.ok("Database Created Successfully");

        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    @Override
    public  ResponseEntity<String> updateDataSource(DatabaseConfigEntity data) {
        try {
            databaseConfigRepository.save(data);
        }catch (Exception e){
            throw new RuntimeException(e.toString());
        }
        return ResponseEntity.ok("Database Created Successfully") ;
    }

    @Override
    public ResponseEntity<String> testConnection(DatabaseConfigEntity data) {

        try {
            Class.forName(data.getDriverClass());

            Properties properties = new Properties();
            properties.setProperty("user", data.getUsername());
            properties.setProperty("password", data.getPassword());

            properties.setProperty("oracle.net.CONNECT_TIMEOUT", "5000");

            try (Connection connection = DriverManager.getConnection(
                    data.getUrl(), properties)) {

                if (connection.isValid(5)) {
                    return ResponseEntity.ok("Database connection successful!");
                }
                throw new RuntimeException("Database connection failed!");

            }

        } catch (ClassNotFoundException e) {

            throw new RuntimeException("Invalid database driver class!");

        } catch (SQLException e) {

            String message = e.getMessage();

            if (message != null && message.contains("ORA-01017")) {
                throw new RuntimeException("Invalid database username or password!");
            }

            if (message != null && message.contains("ORA-12514")) {
                throw new RuntimeException("Database service name is incorrect!");
            }

            if (message != null && message.contains("ORA-12541")) {
                throw new RuntimeException("Database listener is not available!");
            }

            return ResponseEntity.badRequest()
                    .body("Database connection failed. Please check your connection details.");
        }
    }


    @Override
    public List<DatabaseConfigEntity> getDatabaseList() {
        return databaseConfigRepository.findAll();
    }
}
