package com.mislbd.report_manager.utility;

import com.mislbd.report_manager.domain.admin.JasperReportSchedulerDomain;
import com.mislbd.report_manager.enam.ScheduleFrequency;
import com.mislbd.report_manager.entity.admin.JasperReportSchedulerEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

@Service
public final class ScheduleCalculator {

    private ScheduleCalculator() {
    }

    public static LocalDateTime calculateNextRun(
            JasperReportSchedulerEntity schedule,
            LocalDateTime after
    ) {

        if (schedule.getScheduleTime() == null) {
            throw new IllegalArgumentException(
                    "Schedule time is required"
            );
        }

        LocalTime time = LocalTime.parse(schedule.getScheduleTime());

        LocalDate startDate = schedule.getStartDate() != null
                ? schedule.getStartDate()
                : after.toLocalDate();

        LocalDate searchDate = after.toLocalDate();

        if (searchDate.isBefore(startDate)) {
            searchDate = startDate;
        }

        // No schedule should execute beyond end date.
        LocalDate endDate = schedule.getEndDate();

        for (int i = 0; i < 3660; i++) {

            LocalDate candidateDate = searchDate.plusDays(i);

            if (endDate != null && candidateDate.isAfter(endDate)) {
                return null;
            }

            if (!isValidScheduleDate(schedule, candidateDate, startDate)) {
                continue;
            }

            LocalDateTime candidateDateTime =
                    LocalDateTime.of(candidateDate, time);

            if (candidateDateTime.isAfter(after)) {
                return candidateDateTime;
            }
        }

        return null;
    }

    private static boolean isValidScheduleDate(
            JasperReportSchedulerEntity schedule,
            LocalDate candidate,
            LocalDate startDate
    ) {

        ScheduleFrequency frequency =
                schedule.getScheduleFrequency();

        if (frequency == null) {
            return false;
        }

        switch (frequency) {

            case DAILY:
                return true;

            case WEEKLY:
                return isWeeklySchedule(schedule, candidate, startDate);

            case MONTHLY:
                return isMonthlySchedule(schedule, candidate);

            case QUARTERLY:
                return isPeriodicSchedule(schedule, candidate, 3);

            case HALF_YEARLY:
                return isPeriodicSchedule(schedule, candidate, 6);

            case YEARLY:
                return isYearlySchedule(schedule, candidate);

            default:
                return false;
        }
    }

    // -----------------------------------------------
    // WEEKLY
    // -----------------------------------------------

    private static boolean isWeeklySchedule(
            JasperReportSchedulerEntity schedule,
            LocalDate candidate,
            LocalDate startDate
    ) {

        String scheduleDay = schedule.getScheduleDay();

        if (scheduleDay == null || scheduleDay.isBlank()) {
            return false;
        }

        // Simple weekly format:
        // "6" or "1,3,6"
        if (!scheduleDay.contains(":")) {

            int day = candidate.getDayOfWeek().getValue();

            return java.util.Arrays.stream(scheduleDay.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .anyMatch(value -> value == day);
        }

        // Rotating weekly format:
        // "1:6,2:3,3:1,4:4"
        // Week 1 -> Saturday
        // Week 2 -> Wednesday
        // Week 3 -> Monday
        // Week 4 -> Thursday

        int cycle = schedule.getScheduleWeekCycle() != null
                ? schedule.getScheduleWeekCycle()
                : 4;

        if (cycle <= 0) {
            return false;
        }

        LocalDate firstMonday =
                startDate.with(
                        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
                );

        LocalDate candidateMonday =
                candidate.with(
                        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
                );

        long weekDifference =
                java.time.temporal.ChronoUnit.WEEKS.between(
                        firstMonday,
                        candidateMonday
                );

        int cycleWeek = (int) (weekDifference % cycle) + 1;

        int configuredDay = -1;

        for (String item : scheduleDay.split(",")) {

            String[] parts = item.trim().split(":");

            if (parts.length != 2) {
                continue;
            }

            int week = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);

            if (week == cycleWeek) {
                configuredDay = day;
                break;
            }
        }

        return configuredDay == candidate.getDayOfWeek().getValue();
    }

    // -----------------------------------------------
    // MONTHLY
    // -----------------------------------------------

    private static boolean isMonthlySchedule(
            JasperReportSchedulerEntity schedule,
            LocalDate candidate
    ) {

        Integer day = schedule.getScheduleDate();

        if (day == null || day < 1 || day > 31) {
            return false;
        }

        // If configured day is 31, use last day in shorter months.
        int actualDay = Math.min(day, candidate.lengthOfMonth());

        return candidate.getDayOfMonth() == actualDay;
    }

    // -----------------------------------------------
    // QUARTERLY / HALF YEARLY
    // -----------------------------------------------

    private static boolean isPeriodicSchedule(
            JasperReportSchedulerEntity schedule,
            LocalDate candidate,
            int interval
    ) {

        Integer anchorMonth = schedule.getScheduleMonth();
        Integer day = schedule.getScheduleDate();

        if (anchorMonth == null
                || anchorMonth < 1
                || anchorMonth > 12
                || day == null
                || day < 1
                || day > 31) {
            return false;
        }

        int monthDifference =
                Math.floorMod(
                        candidate.getMonthValue() - anchorMonth,
                        12
                );

        if (monthDifference % interval != 0) {
            return false;
        }

        int actualDay = Math.min(day, candidate.lengthOfMonth());

        return candidate.getDayOfMonth() == actualDay;
    }

    // -----------------------------------------------
    // YEARLY
    // -----------------------------------------------

    private static boolean isYearlySchedule(
            JasperReportSchedulerEntity schedule,
            LocalDate candidate
    ) {

        Integer month = schedule.getScheduleMonth();
        Integer day = schedule.getScheduleDate();

        if (month == null || month < 1 || month > 12
                || day == null || day < 1 || day > 31) {
            return false;
        }

        if (candidate.getMonthValue() != month) {
            return false;
        }

        int actualDay = Math.min(day, candidate.lengthOfMonth());

        return candidate.getDayOfMonth() == actualDay;
    }
}