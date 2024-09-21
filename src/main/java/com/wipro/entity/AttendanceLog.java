package com.wipro.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "attendance_log")
public class AttendanceLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	private Integer logId;

	@Column(name = "emp_id")
	private Integer empId;
	@Column(name = "clock_in")
	private LocalTime clockIn;
	@Column(name = "clock_out")
	private LocalTime clockOut;
	@Column(name = "date")
	private LocalDate date;
}
