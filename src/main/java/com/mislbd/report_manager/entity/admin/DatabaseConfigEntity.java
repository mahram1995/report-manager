package com.mislbd.report_manager.entity.admin;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DATABASE_CONFIG")
public class DatabaseConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dbName;
    private String url;
    private String username;
    private String password;
    private String driverClass;
}
