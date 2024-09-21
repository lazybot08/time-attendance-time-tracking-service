package com.wipro.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wipro.entity.AttendanceLog;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Integer> {
	
	boolean existsByEmpId(Integer empId);
	
	@Query("SELECT log FROM AttendanceLog log WHERE log.empId = :empId AND log.clockOut IS NULL")
	AttendanceLog findActiveLogByEmpId(Integer empId);
	
	@Query("SELECT log FROM AttendanceLog log WHERE log.empId = :empId AND log.date BETWEEN :startDate AND :endDate")
	List<AttendanceLog> findLogsByEmpIdAndDateRange(Integer empId, LocalDate startDate, LocalDate endDate);
	
	@Query("SELECT COUNT(a) FROM AttendanceLog a WHERE a.empId = :empId AND a.date BETWEEN :startDate AND :endDate")
	Integer countByEmpIdAndDateRange(@Param("empId") Integer empId, 
	                              @Param("startDate") LocalDate startDate, 
	                              @Param("endDate") LocalDate endDate);

}
