package com.mislbd.report_manager.mapper.admin;

import com.mislbd.report_manager.domain.admin.JasperReportDomain;
import com.mislbd.report_manager.entity.admin.JasperReportEntity;
import com.mislbd.report_manager.entity.admin.JasperReportHistEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JasperReportMapper {

    public JasperReportEntity domainToEntity(JasperReportDomain domain) {

        if (domain == null) {
            return null;
        }

        JasperReportEntity entity = new JasperReportEntity();

        entity.setId(domain.getId());
        entity.setReportFileName(domain.getReportFileName());
        entity.setDataSource(domain.getDataSource());
        entity.setReportContent(domain.getReportContent());

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
        domain.setReportContent(entity.getReportContent());

        return domain;
    }

    public JasperReportHistEntity EntityToHistEntity(Optional<JasperReportEntity> entity) {

        if (entity == null) {
            return null;
        }

        JasperReportHistEntity histEntity = new JasperReportHistEntity();
        histEntity.setReportFileName(entity.get().getReportFileName());
        histEntity.setReportContent(entity.get().getReportContent());

        return histEntity;
    }

    public void updateEntity(
            JasperReportDomain domain,
            JasperReportEntity entity) {

        entity.setReportFileName(domain.getReportFileName());
        entity.setDataSource(domain.getDataSource());
        entity.setReportContent(domain.getReportContent());
    }
}
