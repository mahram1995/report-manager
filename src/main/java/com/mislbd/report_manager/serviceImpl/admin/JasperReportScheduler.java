package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.service.admin.JasperReportSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JasperReportScheduler {

    private final JasperReportSchedulerService schedulerService;

   @Scheduled(cron = "0 * * * * *")
    public void executeScheduledReports() {
       schedulerService.executeDueReports();
   }
}