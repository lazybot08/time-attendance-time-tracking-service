package com.wipro.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wipro.dao.AttendanceLogRepository;
import com.wipro.dto.AttendanceResponseDto;
import com.wipro.dto.DailyLogDto;
import com.wipro.service.TimeService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/entry")
public class TimeRestController {

	@Autowired
	private TimeService timeService;
	
	@Autowired
	private AttendanceLogRepository attendanceLogRepository;

	@PostMapping("/clock-in")
	public ResponseEntity<Void> clockIn(@RequestHeader("role") String role, @RequestParam("empId") Integer empId){
		try {
			timeService.clockInTime(empId);
			return ResponseEntity.status(HttpStatus.OK).build();
		}catch(Exception e) {
			throw e;
		}
	}

	@PostMapping("/clock-out")
	public ResponseEntity<Void> clockOut(HttpServletRequest request){
		try {
			timeService.clockOutTime(request);
			return ResponseEntity.status(HttpStatus.OK).build();
		}catch(RuntimeException e) {
			throw e;
		}catch(Exception e) {
			throw e;
		}
	}

	@GetMapping("/report")
	public ResponseEntity<AttendanceResponseDto> generateAttendanceReport(@RequestHeader("role") String role, @RequestParam Integer empId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
		try {
			AttendanceResponseDto report = timeService.generateAttendanceReport(empId, startDate, endDate);
			return ResponseEntity.ok(report);
		}catch(RuntimeException e) {
			throw e;
		}catch(Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/daily-logs")
	public ResponseEntity<Map<String, Object>> generateDailyLogs(
	        @RequestParam Integer empId, 
	        @RequestParam LocalDate startDate, 
	        @RequestParam LocalDate endDate,
	        @RequestParam int page,
	        @RequestParam int size) {
	    
	    try {
	        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
	            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date range."));
	        }

	        List<DailyLogDto> report = timeService.generateDailyLogs(empId, startDate, endDate, page, size);

	        int totalRecords = attendanceLogRepository.countByEmpIdAndDateRange(empId, startDate, endDate);
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("logs", report);
	        response.put("total", totalRecords);
	        
	        return ResponseEntity.ok(response);
	    } catch (RuntimeException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
	    }
	}


}
