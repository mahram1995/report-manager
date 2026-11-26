package com.mislbd.report_manager.mapper.admin;

import com.mislbd.report_manager.configuration.aopConfig.service.UtilityService;
import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.entity.admin.JasperReportHistEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class JasperReportMapper {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm:ss a");
    private  final UtilityService utilityService ;

    public JasperReportMapper(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    public JasperReportEntity domainToEntity(JasperReportDomain domain) {

        if (domain == null) {
            return null;
        }

        JasperReportEntity entity = new JasperReportEntity();

        entity.setId(domain.getId());
        entity.setReportFileName(domain.getReportFileName());
        entity.setDataSource(domain.getDataSource());
        entity.setReportContent(domain.getReportContent());
        entity.setExecutionTimeoutSeconds(domain.getExecutionTimeoutSeconds());
        entity.setDaysOfQrCodeExpiry(domain.getDaysOfQrCodeExpiry());
        entity.setMaxQrcodeScanLimit(domain.getMaxQrcodeScanLimit());
        entity.setSqlQueryForQrCode(utilityService.jsonToClob(domain.getSqlQueryForQrCode()));


        return entity;
    }

    public JasperReportDomain entityToDomain(JasperReportEntity entity) {

        if (entity == null) {
            return null;
        }

        JasperReportDomain domain = new JasperReportDomain();

        domain.setId(entity.getId());
        domain.setReportFileName(entity.getReportFileName());
        domain.setDataSource(entity.getDataSource());
        domain.setExecutionTimeoutSeconds(entity.getExecutionTimeoutSeconds());

        domain.setCreateDate(
                entity.getCreateDateTime() != null
                        ? entity.getCreateDateTime().format(DATE_FORMATTER)
                        : null
        );

        domain.setVerifyDate(
                entity.getVerifyDateTime() != null
                        ? entity.getVerifyDateTime().format(DATE_FORMATTER)
                        : null
        );

        domain.setUpdateDate(
                entity.getUpdateDateTime() != null
                        ? entity.getUpdateDateTime().format(DATE_FORMATTER)
                        : null
        );
        domain.setCreatedBy(entity.getCreatedBy());
        domain.setVerifyBy(entity.getVerifyBy());
        domain.setUpdateBy(entity.getUpdateBy());
        domain.setDaysOfQrCodeExpiry(entity.getDaysOfQrCodeExpiry());
        domain.setMaxQrcodeScanLimit(entity.getMaxQrcodeScanLimit());
        domain.setSqlQueryForQrCode(utilityService.clobToString(entity.getSqlQueryForQrCode()));

        return domain;
    }

    public JasperReportHistEntity EntityToHistEntity(Optional<JasperReportEntity> entity) {

        if (entity == null) {
            return null;
        }
        JasperReportEntity data=entity.get();
        JasperReportHistEntity histEntity = new JasperReportHistEntity();
        histEntity.setReportFileName(data.getReportFileName());
        histEntity.setReportContent(data.getReportContent());
        histEntity.setExecutionTimeoutSeconds(data.getExecutionTimeoutSeconds());
        histEntity.setDataSource(data.getDataSource());
        histEntity.setCreateAgent(data.getCreateAgent());
        histEntity.setCreateDate(data.getCreateDate());
        histEntity.setCreateDateTime(data.getCreateDateTime());
        histEntity.setCreatedBy(data.getCreatedBy());
        histEntity.setCreatedTerminal(data.getCreatedTerminal());
        histEntity.setVerifyAgent(data.getVerifyAgent());
        histEntity.setVerifyBy(data.getVerifyBy());
        histEntity.setVerifyDate(data.getVerifyDate());
        histEntity.setVerifyTerminal(data.getVerifyTerminal());
        histEntity.setVerifyDateTime(data.getVerifyDateTime());
        histEntity.setUpdateAgent(data.getUpdateAgent());
        histEntity.setUpdateBy(data.getUpdateBy());
        histEntity.setUpdateDate(data.getUpdateDate());
        histEntity.setUpdateTerminal(data.getUpdateTerminal());
        histEntity.setUpdateDateTime(data.getUpdateDateTime());
        histEntity.setDaysOfQrCodeExpiry(data.getDaysOfQrCodeExpiry());
        histEntity.setMaxQrcodeScanLimit(data.getMaxQrcodeScanLimit());
        histEntity.setSqlQueryForQrCode(data.getSqlQueryForQrCode());
        return histEntity;
    }


}
