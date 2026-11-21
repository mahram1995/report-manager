package com.mislbd.report_manager.mapper.admin;

import com.mislbd.report_manager.domain.admin.JasperReportSchedulerDomain;
import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import org.springframework.stereotype.Component;

@Component
public class JasperReportSchedulerMapper {

    public JasperReportSchedulerDomain toDomain(
            JasperReportSchedulerEntity entity
    ) {
        JasperReportSchedulerDomain domain =
                new JasperReportSchedulerDomain();

        domain.setId(entity.getId());
        domain.setReportFileName(entity.getReportFileName());
        domain.setReportFormat(entity.getReportFormat());
        domain.setReportParameters(entity.getReportParameters());
        domain.setEmailAddressTo(entity.getEmailAddressTo());
        domain.setEmailAddressCc(entity.getEmailAddressCc());
        domain.setIsSendEmailSeparately(entity.getIsSendEmailSeparately());

        domain.setEmailSubject(entity.getEmailSubject());
        domain.setEmailBody(entity.getEmailBody());
        domain.setEmailSignature(entity.getEmailSignature());

        domain.setScheduleTime(entity.getScheduleTime());
        domain.setScheduleFrequency(entity.getScheduleFrequency());
        domain.setScheduleDate(entity.getScheduleDate());
        domain.setScheduleDay(entity.getScheduleDay());
        domain.setScheduleMonth(entity.getScheduleMonth());
        domain.setScheduleWeekCycle(entity.getScheduleWeekCycle());

        domain.setStartDate(entity.getStartDate());
        domain.setEndDate(entity.getEndDate());

        domain.setIsActive(entity.getIsActive());

        domain.setNextRunAt(entity.getNextRunAt());
        domain.setLastRunAt(entity.getLastRunAt());
        domain.setLastStatus(entity.getLastStatus());
        domain.setLastError(entity.getLastError());

        return domain;
    }
    public JasperReportSchedulerEntity toEntity(JasperReportSchedulerDomain domain) {

        JasperReportSchedulerEntity entity = new JasperReportSchedulerEntity();

        entity.setId(domain.getId());
        entity.setReportFileName(domain.getReportFileName());
        entity.setReportFormat(domain.getReportFormat());
        entity.setReportParameters(domain.getReportParameters());
        entity.setEmailAddressTo(domain.getEmailAddressTo());
        entity.setEmailAddressCc(domain.getEmailAddressCc());
        entity.setIsSendEmailSeparately(domain.getIsSendEmailSeparately());

        entity.setEmailSubject(domain.getEmailSubject());
        entity.setEmailBody(domain.getEmailBody());
        entity.setEmailSignature(domain.getEmailSignature());

        entity.setScheduleTime(domain.getScheduleTime());
        entity.setScheduleFrequency(domain.getScheduleFrequency());
        entity.setScheduleDate(domain.getScheduleDate());
        entity.setScheduleDay(domain.getScheduleDay());
        entity.setScheduleMonth(domain.getScheduleMonth());
        entity.setScheduleWeekCycle(domain.getScheduleWeekCycle());

        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());

        entity.setIsActive(domain.getIsActive());

        entity.setNextRunAt(domain.getNextRunAt());
        entity.setLastRunAt(domain.getLastRunAt());
        entity.setLastStatus(domain.getLastStatus());
        entity.setLastError(domain.getLastError());

        return entity;
    }
}
