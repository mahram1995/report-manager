package com.mislbd.report_manager.mapper.admin;

import com.mislbd.report_manager.configuration.aopConfig.service.UtilityService;
import com.mislbd.report_manager.domain.admin.JasperReportAuditDomain;
import com.mislbd.report_manager.entity.admin.JasperReportAuditLogEntity;
import org.springframework.stereotype.Component;

@Component
public class JasperReportAuditLogMapper {
    private final UtilityService utilityService;

    public JasperReportAuditLogMapper(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    public JasperReportAuditLogEntity domainToEntity(JasperReportAuditDomain domain) {

        if (domain == null) {
            return null;
        }

        JasperReportAuditLogEntity entity = new JasperReportAuditLogEntity();

        entity.setId(domain.getId());
        entity.setReportFileName(domain.getReportFileName());
        entity.setDataSource(domain.getDataSource());
        entity.setReportName(domain.getReportName());
        entity.setStatus(domain.getStatus());
        entity.setReportData(domain.getReportData());
        entity.setError(domain.getError());
        entity.setEntryUser(domain.getEntryUser());
        entity.setEntryDate(domain.getEntryDate());
        entity.setTerminal(domain.getTerminal());
        entity.setUserClient(domain.getUserClient());
        entity.setDurationInms(domain.getDurationInms());
        entity.setStartTime(domain.getStartTime());
        entity.setEndTime(domain.getEndTime());
        entity.setParameter(utilityService.jsonToClob(domain.getParameter()));
        entity.setReportFormat(domain.getReportFormat());

        return entity;
    }

    public JasperReportAuditDomain entityToDomain(JasperReportAuditLogEntity entity) {

        if (entity == null) {
            return null;
        }

        JasperReportAuditDomain domain = new JasperReportAuditDomain();

        domain.setId(entity.getId());
        domain.setReportFileName(entity.getReportFileName());
        domain.setDataSource(entity.getDataSource());
        domain.setReportName(entity.getReportName());
        domain.setStatus(entity.getStatus());
        domain.setEntryUser(entity.getEntryUser());
        domain.setEntryDate(entity.getEntryDate());
        domain.setTerminal(entity.getTerminal());
        domain.setUserClient(entity.getUserClient());
        domain.setStartTime(entity.getStartTime());
        domain.setEndTime(entity.getEndTime());
        domain.setDurationInms(entity.getDurationInms());
        domain.setParameter(utilityService.clobToJson(entity.getParameter()));
        domain.setReportFormat(entity.getReportFormat());


        return domain;
    }
}
