package com.wipro.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.dao.AttendanceLogRepository;
import com.wipro.dto.AttendanceResponseDto;
import com.wipro.dto.DailyLogDto;
import com.wipro.entity.AttendanceLog;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TimeServiceImpl implements TimeService {

	@Autowired
	private AttendanceLogRepository attendanceLogRepository;

	@Override
	public void clockInTime(Integer empId) {
		LocalTime currentTime = LocalTime.now();
		LocalDate currentDate = LocalDate.now();

		AttendanceLog log = new AttendanceLog();
		log.setEmpId(empId);
		log.setClockIn(currentTime);
		log.setDate(currentDate);

		attendanceLogRepository.save(log);
	}

	@Override
	public void clockOutTime(HttpServletRequest request) {
		if (request.getHeader("empId") == null || request.getHeader("empId").isEmpty()) {
			throw new IllegalArgumentException("Missing or empty 'empId' header");
		}
		Integer empId = Integer.parseInt(request.getHeader("empId"));
		LocalTime currentTime = LocalTime.now();

		AttendanceLog log = attendanceLogRepository.findActiveLogByEmpId(empId);
		if(log!=null) {
			log.setClockOut(currentTime);

			attendanceLogRepository.save(log);
		}else {
			throw new RuntimeException("No active clock-in found for the empployee.");
		}
	}

	@Override
	public AttendanceResponseDto generateAttendanceReport(Integer empId, LocalDate startDate, LocalDate endDate) {

		if (!attendanceLogRepository.existsByEmpId(empId)) {
			throw new RuntimeException("Invalid employee ID.");
		}

		LocalDate today = LocalDate.now();

		startDate = (startDate == null || !startDate.isBefore(today)) ? today.minusDays(1) : startDate;
		endDate = (endDate == null || !endDate.isBefore(today)) ? today.minusDays(1) : endDate;

		if (endDate.isBefore(startDate)) {
			throw new RuntimeException("Invalid date range.");
		}

		List<AttendanceLog> logs = attendanceLogRepository.findLogsByEmpIdAndDateRange(empId, startDate, endDate);

		if (logs.isEmpty()) {
			throw new RuntimeException("No attendance logs found for the specified date range.");
		}

		Map<LocalDate, List<AttendanceLog>> logsByDate = logs.stream()
				.collect(Collectors.groupingBy(AttendanceLog::getDate));

		AttendanceResponseDto report = new AttendanceResponseDto();
		report.setEmpId(empId);

		logsByDate.forEach((date, logsForDate) -> {
			DailyLogDto dailyLog = createDailyLogDto(date, logsForDate);
			report.addDailyLog(dailyLog);
		});

		return report;
	}

	@Override
	public List<DailyLogDto> generateDailyLogs(Integer empId, LocalDate startDate, LocalDate endDate, int page, int size) {
		if (!attendanceLogRepository.existsByEmpId(empId)) {
			throw new RuntimeException("Invalid employee ID.");
		}

		LocalDate today = LocalDate.now();

		startDate = (startDate == null || !startDate.isBefore(today)) ? today.minusDays(1) : startDate;
		endDate = (endDate == null || !endDate.isBefore(today)) ? today.minusDays(1) : endDate;

		if (endDate.isBefore(startDate)) {
			throw new RuntimeException("Invalid date range.");
		}

	    List<DailyLogDto> response = new ArrayList<>();
	    List<AttendanceLog> logs = attendanceLogRepository.findLogsByEmpIdAndDateRange(empId, startDate, endDate);

	    if (logs.isEmpty()) {
	        throw new RuntimeException("No attendance logs found for the specified date range.");
	    }

	    Map<LocalDate, List<AttendanceLog>> logsByDate = logs.stream()
	            .collect(Collectors.groupingBy(AttendanceLog::getDate));

	    logsByDate.forEach((date, logsForDate) -> {
	        DailyLogDto dailyLog = createDailyLogDto(date, logsForDate);
	        response.add(dailyLog);
	    });

	    int totalRecords = response.size();
	    int totalPages = (int) Math.ceil((double) totalRecords / size);

	    if (page < 0 || page >= totalPages) {
	        throw new RuntimeException("Invalid page number.");
	    }

	    int fromIndex = page * size;
	    int toIndex = Math.min(fromIndex + size, totalRecords);
	    return response.subList(fromIndex, toIndex);
	}


	private DailyLogDto createDailyLogDto(LocalDate date, List<AttendanceLog> logsForDate) {
		LocalTime earliestClockIn = logsForDate.stream()
				.map(AttendanceLog::getClockIn)
				.min(LocalTime::compareTo)
				.orElse(null);

		LocalTime latestClockOut = logsForDate.stream()
				.map(AttendanceLog::getClockOut)
				.max(LocalTime::compareTo)
				.orElse(null);

		DailyLogDto dailyLog = new DailyLogDto();
		dailyLog.setWorkDate(date);
		dailyLog.setClockInTime(earliestClockIn);
		dailyLog.setClockOutTime(latestClockOut);
		dailyLog.calculateMetrics();

		return dailyLog;
	}
}
