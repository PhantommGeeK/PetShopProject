package com.cg.service;

import java.util.List;

import com.cg.dto.EmployeeRequestDTO;
import com.cg.dto.EmployeeResponseDTO;
import com.cg.dto.SuccessDTO;

public interface EmployeeService {
	
	SuccessDTO createEmployee(EmployeeRequestDTO requestDTO);

	EmployeeResponseDTO getEmployeeById(Integer employeeId);

    List<EmployeeResponseDTO> getAllEmployees();

    List<EmployeeResponseDTO> getEmployeesByPosition(String position);

    SuccessDTO updateEmployee(int employeeId, EmployeeRequestDTO requestDTO);

    SuccessDTO deleteEmployee(int employeeId);

}
