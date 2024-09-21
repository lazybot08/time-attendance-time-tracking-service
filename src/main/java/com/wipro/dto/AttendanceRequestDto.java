package com.wipro.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttendanceRequestDto {
	private Integer empId;
	private LocalDate startDate;
	private LocalDate endDate;
}
