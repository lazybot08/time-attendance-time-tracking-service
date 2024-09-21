package com.wipro.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttendanceResponseDto {
    private Integer empId;
    private List<DailyLogDto> dailyLogs = new ArrayList<>();
    private Double totalWorkHours = 0.0;
    private Double totalNetHours = 0.0;
    private Double totalOvertimeHours = 0.0;

    public void addDailyLog(DailyLogDto dailyLog) {
        dailyLog.calculateMetrics();
        this.dailyLogs.add(dailyLog);
        this.totalWorkHours += dailyLog.getWorkHours();
        this.totalNetHours += dailyLog.getNetHours();
        this.totalOvertimeHours += dailyLog.getOvertimeHours();
        this.totalWorkHours = roundToTwoDecimalPlaces(this.totalWorkHours);
        this.totalNetHours = roundToTwoDecimalPlaces(this.totalNetHours);
        this.totalOvertimeHours = roundToTwoDecimalPlaces(this.totalOvertimeHours);
    }

    private double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
