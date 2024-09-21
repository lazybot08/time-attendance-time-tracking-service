package com.wipro.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DailyLogDto {
    private LocalDate workDate;
    private LocalTime clockInTime;
    private LocalTime clockOutTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
    private Double workHours;
    private Double netHours;
    private Double overtimeHours;

    public void calculateMetrics() {
        if (clockInTime != null && clockOutTime != null) {
            Duration workDuration = Duration.between(clockInTime, clockOutTime);
            Duration breakDuration = Duration.ZERO;
            if (breakStartTime != null && breakEndTime != null) {
                breakDuration = Duration.between(breakStartTime, breakEndTime);
            }
            Duration netDuration = workDuration.minus(breakDuration);
            this.workHours = roundToTwoDecimalPlaces(workDuration.toHours() + workDuration.toMinutesPart() / 60.0);
            this.netHours = roundToTwoDecimalPlaces(netDuration.toHours() + netDuration.toMinutesPart() / 60.0);
            this.overtimeHours = calculateOvertimeHours();
        } else {
            this.workHours = 0.0;
            this.netHours = 0.0;
            this.overtimeHours = 0.0;
        }
    }

    private Double calculateOvertimeHours() {
        final double standardWorkingHours = 9.0;
        return (this.workHours > standardWorkingHours) ? roundToTwoDecimalPlaces(this.workHours - standardWorkingHours) : 0.0;
    }

    private double roundToTwoDecimalPlaces(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
