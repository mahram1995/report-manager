package com.mislbd.report_manager.utility;

import com.mislbd.report_manager.entity.admin.DatabaseConfigEntity;
import com.mislbd.report_manager.service.admin.AppConfigurationService;
import com.mislbd.report_manager.service.admin.JasperDataSourceService;
import com.zaxxer.hikari.HikariDataSource;
import org.jfree.util.Log;

import javax.sql.DataSource;
import java.util.Optional;

public class AppConfig {

    private static AppConfigurationService getService() {
        return AppContext.getBean(AppConfigurationService.class);
    }
    private static JasperDataSourceService getDataSourceService() {
        return AppContext.getBean(JasperDataSourceService.class);
    }

    public static String getConfigurationValue(String itemName) {
        return getService().getAppConfigurationValue(itemName);
    }

    public static DataSource createDataSource(Long  dataBaseId) {
        DatabaseConfigEntity db = getDataSourceService().getDataBase(dataBaseId);
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(db.getUrl());
        dataSource.setUsername(db.getUsername());
        dataSource.setPassword(db.getPassword());
        dataSource.setDriverClassName(db.getDriverClass());

        // For report execution
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(1);

        return dataSource;
    }


}