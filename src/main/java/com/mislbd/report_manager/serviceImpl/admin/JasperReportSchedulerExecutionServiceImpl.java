package com.mislbd.report_manager.serviceImpl.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import com.mislbd.report_manager.service.admin.JasperReportSchedulerExecutionService;
import com.mislbd.report_manager.service.admin.JasperReportService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class JasperReportSchedulerExecutionServiceImpl implements JasperReportSchedulerExecutionService {

    private final JasperReportService jasperReportService;
    private final ObjectMapper objectMapper;

    @Override
    public byte[] generateReport(
            JasperReportSchedulerEntity scheduler
    ) {

        try {
            JasperReportRequestDomain request = buildReportRequest(scheduler);

            // Reuse the existing report generation pipeline.
            ResponseEntity<byte[]> response = jasperReportService.runReport(request);

            if (response == null || response.getBody() == null
                    || response.getBody().length == 0) {
                throw new IllegalStateException(
                        "Scheduled report generated an empty result: "
                                + scheduler.getReportFileName()
                );
            }

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    JasperReportRequestDomain buildReportRequest(
            JasperReportSchedulerEntity scheduler
    ) {
        try {

            JasperReportRequestDomain request =
                    new JasperReportRequestDomain();

            request.setJasperFileName(
                    scheduler.getReportFileName()
            );

            // No separate report display-name field in the scheduler
            // currently; use the file name until you add one.
            request.setReportName(
                    scheduler.getReportFileName()
            );

            request.setReportFormat(
                    scheduler.getReportFormat() != null
                            ? scheduler.getReportFormat()
                            : "pdf"
            );

            Map<String, Object> parameters = new HashMap<>();

            if (scheduler.getReportParameters() != null
                    && !scheduler.getReportParameters().isBlank()) {

                parameters = objectMapper.readValue(
                        scheduler.getReportParameters(),
                        new TypeReference<Map<String, Object>>() {
                        }
                );
            }

            request.setParameters(parameters);

            return request;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
