package com.mislbd.report_manager.domain.admin;

import com.mislbd.report_manager.enam.ScheduleFrequency;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class JasperReportSchedulerDomain {

    private Long id;

    private String reportFileName;
    private String reportFormat;

    private String emailAddressTo;
    private String emailAddressCc;

    private Boolean isSendEmailSeparately;

    private String emailSubject;
    private String emailBody;
    private String emailSignature;

    // HH:mm
    private String scheduleTime;

    private ScheduleFrequency scheduleFrequency;

    // Monthly/quarterly/half-yearly/yearly execution day
    private Integer scheduleDate;

    // Weekly configuration
    // Example: "6" or "1,3,6" or "1:6,2:3,3:1,4:4"
    private String scheduleDay;

    // Anchor month for quarterly/half-yearly/yearly
    private Integer scheduleMonth;

    // Number of weeks in a repeating weekly cycle
    private Integer scheduleWeekCycle;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isActive;

    private LocalDateTime nextRunAt;
    private LocalDateTime lastRunAt;

    private String lastStatus;
    private String lastError;
}
