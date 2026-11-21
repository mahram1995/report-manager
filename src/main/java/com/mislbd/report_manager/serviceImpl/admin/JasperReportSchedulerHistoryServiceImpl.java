package com.mislbd.report_manager.serviceImpl.admin;

import com.mislbd.report_manager.entity.admin.JasperReportScheduleExecutionEntity;
import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import com.mislbd.report_manager.repository.admin.JasperReportSchedulerHistoryRepository;
import com.mislbd.report_manager.service.admin.JasperReportSchedulerHistoryService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JasperReportSchedulerHistoryServiceImpl implements JasperReportSchedulerHistoryService {
    private final JasperReportSchedulerHistoryRepository repository;

    public JasperReportSchedulerHistoryServiceImpl(JasperReportSchedulerHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveFailure(JasperReportSchedulerEntity schedule, LocalDateTime startTime, LocalDateTime endTime, Exception ex) {
        try {
            JasperReportScheduleExecutionEntity entity=new JasperReportScheduleExecutionEntity();
               entity.setSchedulerId(schedule.getId());
               entity.setExecutionStartTime(startTime);
               entity.setExecutionStatus("FAIL");
               entity.setExecutionEndTime(endTime);
               entity.setErrorMessage(ex.getMessage());
               entity.setReportFileName(schedule.getReportFileName());

               repository.save(entity);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void saveSuccess(JasperReportSchedulerEntity schedule, LocalDateTime startTime, LocalDateTime endTime, Integer length, String emailTo,
                            String emailCC, Boolean isEmailSet) {
        try {
            JasperReportScheduleExecutionEntity entity=new JasperReportScheduleExecutionEntity();
            entity.setSchedulerId(schedule.getId());
            entity.setExecutionStartTime(startTime);
            entity.setExecutionStatus("SUCCESS");
            entity.setExecutionEndTime(endTime);
            entity.setReportFileName(schedule.getReportFileName());
            entity.setEmailSentTo(emailTo +";"+emailTo);
            entity.setEmailSent(isEmailSet);

            repository.save(entity);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
