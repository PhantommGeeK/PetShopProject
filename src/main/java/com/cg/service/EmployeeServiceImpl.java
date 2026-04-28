package com.cg.service;



import com.cg.dto.EmployeeRequestDTO;
import com.cg.dto.EmployeeResponseDTO;
import com.cg.entity.Addresses;
import com.cg.entity.Employee;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressesRepository;
import com.cg.repo.EmployeeRepository;
import com.cg.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AddressesRepository addressesRepository;

    // ---------------------- Helper: Entity -> ResponseDTO ----------------------
    private EmployeeResponseDTO mapToResponseDTO(Employee employee) {
        Integer addressId = null;
        String city      = null;
        String state     = null;

        if (employee.getAddress() != null) {
            addressId = employee.getAddress().getAddressId();
            city      = employee.getAddress().getCity();
            state     = employee.getAddress().getState();
        }

        return new EmployeeResponseDTO(
        );
    }

    // ---------------------- Create ----------------------
    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        Addresses address = addressesRepository.findById(requestDTO.getAddressId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Address not found with ID: " + requestDTO.getAddressId()));

        Employee employee = new Employee();
        employee.setFirstName(requestDTO.getFirstName());
        employee.setLastName(requestDTO.getLastName());
        employee.setPosition(requestDTO.getPosition());
        employee.setHireDate(requestDTO.getHireDate());
        employee.setPhoneNumber(requestDTO.getPhoneNumber());
        employee.setEmail(requestDTO.getEmail());
        employee.setAddress(address);

        Employee saved = employeeRepository.save(employee);
        return mapToResponseDTO(saved);
    }

    // ---------------------- Get By ID ----------------------
    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getEmployeeById(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with ID: " + employeeId));
        return mapToResponseDTO(employee);
    }

    // ---------------------- Get All ----------------------
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
            .stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    // ---------------------- Get By Position ----------------------
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getEmployeesByPosition(String position) {
        return employeeRepository.findByPositionIgnoreCase(position)
            .stream()
            .map(this::mapToResponseDTO)   // ✅ perfectly fine
            .collect(Collectors.toList());
    }

    // ---------------------- Update ----------------------
    @Override
    public EmployeeResponseDTO updateEmployee(int employeeId, EmployeeRequestDTO requestDTO) {
        Employee existing = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with ID: " + employeeId));

        Addresses address = addressesRepository.findById(requestDTO.getAddressId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Address not found with ID: " + requestDTO.getAddressId()));

        existing.setFirstName(requestDTO.getFirstName());
        existing.setLastName(requestDTO.getLastName());
        existing.setPosition(requestDTO.getPosition());
        existing.setHireDate(requestDTO.getHireDate());
        existing.setPhoneNumber(requestDTO.getPhoneNumber());
        existing.setEmail(requestDTO.getEmail());
        existing.setAddress(address);

        Employee updated = employeeRepository.save(existing);
        return mapToResponseDTO(updated);
    }

    // ---------------------- Delete ----------------------
    @Override
    public void deleteEmployee(int employeeId) {
        Employee existing = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Employee not found with ID: " + employeeId));
        employeeRepository.delete(existing);
    }
}