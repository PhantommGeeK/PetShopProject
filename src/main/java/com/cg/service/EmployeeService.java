package com.cg.service;

import java.util.List;

import com.cg.dto.EmployeeRequestDTO;
import com.cg.dto.EmployeeResponseDTO;

public interface EmployeeService {
	
	EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO);

    EmployeeResponseDTO getEmployeeById(int employeeId);

    List<EmployeeResponseDTO> getAllEmployees();

    List<EmployeeResponseDTO> getEmployeesByPosition(String position);

    EmployeeResponseDTO updateEmployee(int employeeId, EmployeeRequestDTO requestDTO);

    void deleteEmployee(int employeeId);

}
