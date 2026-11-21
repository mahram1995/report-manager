package com.mislbd.report_manager.serviceImpl.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mislbd.report_manager.domain.admin.JasperReportRequestDomain;
import com.mislbd.report_manager.domain.admin.JasperReportSchedulerDomain;
import com.mislbd.report_manager.entity.admin.JasperReportScheduleExecutionEntity;
import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import com.mislbd.report_manager.mapper.admin.JasperReportSchedulerMapper;
import com.mislbd.report_manager.repository.admin.JasperReportSchedulerHistoryRepository;
import com.mislbd.report_manager.repository.admin.JasperReportSchedulerRepository;
import com.mislbd.report_manager.service.admin.*;
import com.mislbd.report_manager.utility.ScheduleCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JasperReportSchedulerServiceImpl implements JasperReportSchedulerService {
    private final JasperReportSchedulerRepository repository;
    private final JasperReportSchedulerMapper mapper;
    private final JasperReportSchedulerHistoryService historyService;
    private final JasperReportEmailService reportEmailService;
    private  final ObjectMapper objectMapper;
    private final JasperReportService reportService;
    private  final JasperReportSchedulerHistoryRepository scheduleLogRepo;



    public void executeDueReports() {

        LocalDateTime now = LocalDateTime.now();

        List<JasperReportSchedulerEntity> schedules =
                repository
                        .findByIsActiveTrueAndNextRunAtLessThanEqual(now);

        for (JasperReportSchedulerEntity schedule : schedules) {
            executeSingleSchedule(schedule);
        }
    }

    @Override
    public ResponseEntity<Object> checkReportSchedule(Long scheduleId) {

        try{
            executeSingleSchedule(repository.findById(scheduleId).get());
            return ResponseEntity.ok("Schedule Report Generate Successfully");
        }catch (Exception e )
        {
            return ResponseEntity.ok("Schedule Report not Generate");
        }

    }


    public byte[] executeReport(JasperReportSchedulerEntity schedule) {
        return new byte[0];
    }

    private void executeSingleSchedule(JasperReportSchedulerEntity schedule) {

        LocalDateTime startTime = LocalDateTime.now();

        try {
            String reportParameters = schedule.getReportParameters();
            Map<String, Object> parameters = new HashMap<>();
            if (reportParameters != null && !reportParameters.isBlank()) {
                parameters = objectMapper.readValue(
                        reportParameters,
                        new TypeReference<Map<String, Object>>() {}
                );
            }

            JasperReportRequestDomain reportRequest=new JasperReportRequestDomain();
            reportRequest.setReportFormat(schedule.getReportFormat());
            reportRequest.setReportName(schedule.getReportFileName());
            reportRequest.setJasperFileName(schedule.getReportFileName());
            reportRequest.setParameters(parameters);

            // 1. Execute report
            byte[] reportData = reportService.runReport(reportRequest).getBody();


            // 2. Send email only if report execution succeeds
         Boolean isEmailSent=   reportEmailService.sendReport(
                    schedule.getEmailAddressTo(),
                    schedule.getEmailAddressCc(),
                    Boolean.TRUE.equals(
                            schedule.getIsSendEmailSeparately()
                    ),
                    schedule.getEmailSubject(),
                    schedule.getEmailBody(),
                    schedule.getEmailSignature(),
                    schedule.getReportFileName().split("\\.")[0] + "." + schedule.getReportFormat(),
                    reportData
            );

            // 3. Update successful execution
            schedule.setLastRunAt(startTime);
            schedule.setLastStatus("SUCCESS");
            schedule.setLastError(null);


            // 4. Calculate next execution time
            schedule.setNextRunAt(ScheduleCalculator.calculateNextRun(schedule, startTime));

            historyService.saveSuccess(
                    schedule,
                    startTime,
                    LocalDateTime.now(),
                    reportData.length,
                    schedule.getEmailAddressTo(),
                    schedule.getEmailAddressCc(),
                    isEmailSent

            );

        } catch (Exception ex) {

            schedule.setLastRunAt(startTime);
            schedule.setLastStatus("FAILED");
            schedule.setLastError(ex.getMessage());

            // Avoid retrying continuously every minute
            schedule.setNextRunAt(
                    ScheduleCalculator.calculateNextRun(
                            schedule,
                            startTime
                    )
            );

            historyService.saveFailure(
                    schedule,
                    startTime,
                    LocalDateTime.now(),
                    ex
            );
        }

        repository.save(schedule);
    }

    public JasperReportSchedulerDomain save(
            JasperReportSchedulerDomain domain
    ) {

        validate(domain);

        LocalDateTime now = LocalDateTime.now();

        domain.setIsActive(
                domain.getIsActive() == null
                        ? true
                        : domain.getIsActive()
        );

        domain.setScheduleWeekCycle(
                domain.getScheduleWeekCycle() == null
                        ? 4
                        : domain.getScheduleWeekCycle()
        );

        // Calculate first execution
        domain.setNextRunAt(
                ScheduleCalculator.calculateNextRun(
                        mapper.toEntity(domain),
                        now.minusSeconds(1)
                )
        );

        domain.setLastStatus("PENDING");

        JasperReportSchedulerEntity entity = mapper.toEntity(domain);

        JasperReportSchedulerEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    public JasperReportSchedulerDomain update(
            JasperReportSchedulerDomain domain
    ) {
        long id=domain.getId();

        JasperReportSchedulerEntity existing =
                repository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(    "Scheduler not found: " + id  ));

        domain.setId(id);

        validate(domain);

        domain.setLastRunAt(existing.getLastRunAt());
        domain.setLastStatus(existing.getLastStatus());
        domain.setLastError(existing.getLastError());

        domain.setIsActive(
                domain.getIsActive() != null
                        ? domain.getIsActive()
                        : existing.getIsActive()
        );

        domain.setScheduleWeekCycle(
                domain.getScheduleWeekCycle() == null
                        ? 4
                        : domain.getScheduleWeekCycle()
        );

        domain.setNextRunAt(
                Boolean.TRUE.equals(domain.getIsActive())
                        ? ScheduleCalculator.calculateNextRun(mapper.toEntity(domain),
                        LocalDateTime.now().minusSeconds(1)
                )
                        : null
        );

        JasperReportSchedulerEntity entity = mapper.toEntity(domain);

        JasperReportSchedulerEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Transactional(readOnly = true)
    public JasperReportSchedulerDomain getById(Long id) {

        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Scheduler not found: " + id
                        ));
    }

    @Transactional(readOnly = true)
    public List<JasperReportSchedulerDomain> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Scheduler not found: " + id
            );
        }

        repository.deleteById(id);
    }

    public JasperReportSchedulerDomain activate(Long id) {

        JasperReportSchedulerEntity entity = getEntity(id);

        entity.setIsActive(true);

        entity.setNextRunAt(
                ScheduleCalculator.calculateNextRun(
                        entity,
                        LocalDateTime.now().minusSeconds(1)
                )
        );

        return mapper.toDomain(repository.save(entity));
    }

    public JasperReportSchedulerDomain deactivate(Long id) {

        JasperReportSchedulerEntity entity = getEntity(id);

        entity.setIsActive(false);
        entity.setNextRunAt(null);

        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<JasperReportScheduleExecutionEntity> getScheduleExecutionLog() {
        return scheduleLogRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    private JasperReportSchedulerEntity getEntity(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Scheduler not found: " + id
                        ));
    }


    private void validate(
            JasperReportSchedulerDomain donaim
    ) {

        if (donaim.getReportFileName() == null
                || donaim.getReportFileName().isBlank()) {
            throw new IllegalArgumentException(
                    "Report file name is required"
            );
        }

        if (donaim.getScheduleTime() == null) {
            throw new IllegalArgumentException("Schedule time is required");
        }

        // Validate HH:mm
        LocalTime.parse(donaim.getScheduleTime());

        if (donaim.getScheduleFrequency() == null) {
            throw new IllegalArgumentException("Schedule frequency is required");
        }

        if (donaim.getStartDate() != null
                && donaim.getEndDate() != null
                && donaim.getEndDate().isBefore(donaim.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        if (donaim.getScheduleFrequency().name().equals("WEEKLY")) {
            validateWeekly(donaim);
        }

        switch (donaim.getScheduleFrequency()) {

            case MONTHLY:
            case QUARTERLY:
            case HALF_YEARLY:
            case YEARLY:

                if (donaim.getScheduleDate() == null
                        || donaim.getScheduleDate() < 1
                        || donaim.getScheduleDate() > 31) {
                    throw new IllegalArgumentException(
                            "Schedule date must be between 1 and 31"
                    );
                }
                break;

            default:
                break;
        }

        switch (donaim.getScheduleFrequency()) {

            case QUARTERLY:
            case HALF_YEARLY:
            case YEARLY:

                if (donaim.getScheduleMonth() == null
                        || donaim.getScheduleMonth() < 1
                        || donaim.getScheduleMonth() > 12) {
                    throw new IllegalArgumentException(
                            "Schedule month must be between 1 and 12"
                    );
                }
                break;

            default:
                break;
        }
    }

    private void validateWeekly(
            JasperReportSchedulerDomain domain
    ) {

        String scheduleDay = domain.getScheduleDay();

        if (scheduleDay == null || scheduleDay.isBlank()) {
            throw new IllegalArgumentException(
                    "Weekly schedule day is required"
            );
        }

        int cycle = domain.getScheduleWeekCycle() == null
                ? 4
                : domain.getScheduleWeekCycle();

        if (cycle < 1 || cycle > 52) {
            throw new IllegalArgumentException(
                    "Weekly cycle must be between 1 and 52"
            );
        }

        // Simple format: 1,3,6
        if (!scheduleDay.contains(":")) {

            for (String day : scheduleDay.split(",")) {
                int value = Integer.parseInt(day.trim());

                if (value < 1 || value > 7) {
                    throw new IllegalArgumentException(
                            "Weekday must be between 1 and 7"
                    );
                }
            }

            return;
        }

        // Rotating format: 1:6,2:3,3:1,4:4
        for (String item : scheduleDay.split(",")) {

            String[] parts = item.trim().split(":");

            if (parts.length != 2) {
                throw new IllegalArgumentException(
                        "Invalid weekly pattern: " + item
                );
            }

            int week = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);

            if (week < 1 || week > cycle) {
                throw new IllegalArgumentException(
                        "Week must be between 1 and " + cycle
                );
            }

            if (day < 1 || day > 7) {
                throw new IllegalArgumentException(
                        "Weekday must be between 1 and 7"
                );
            }
        }
    }
}
