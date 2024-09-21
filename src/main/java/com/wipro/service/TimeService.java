package com.wipro.service;

import java.time.LocalDate;
import java.util.List;

import com.wipro.dto.AttendanceResponseDto;
import com.wipro.dto.DailyLogDto;

import jakarta.servlet.http.HttpServletRequest;

public interface TimeService {
	void clockInTime(Integer empId);
	void clockOutTime(HttpServletRequest request);
	AttendanceResponseDto generateAttendanceReport(Integer empId, LocalDate startDate, LocalDate endDate);
	List<DailyLogDto> generateDailyLogs(Integer empId, LocalDate startDate, LocalDate endDate, int page, int size); 
}
