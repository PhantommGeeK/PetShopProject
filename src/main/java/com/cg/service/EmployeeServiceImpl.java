package com.cg.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.AddressesResponseDTO;
import com.cg.dto.EmployeeRequestDTO;
import com.cg.dto.EmployeeResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.Addresses;
import com.cg.entity.Employee;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressesRepository;
import com.cg.repo.EmployeeRepository;


@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	 @Autowired
	    private EmployeeRepository employeeRepository;
	 
	 @Autowired
	   	private AddressesRepository addressRepository;


	 private EmployeeResponseDTO mapToDTO(Employee emp) {

	        AddressesResponseDTO addressDTO = new AddressesResponseDTO(
	                emp.getAddress().getAddressId(),
	                emp.getAddress().getStreet(),
	                emp.getAddress().getCity(),
	                emp.getAddress().getState(),
	                emp.getAddress().getZipCode()
	        );

	        return new EmployeeResponseDTO(
	                emp.getEmployeeId(),
	                emp.getFirstName(),
	                emp.getLastName(),
	                emp.getPosition(),
	                emp.getHireDate(),
	                emp.getPhoneNumber(),
	                emp.getEmail(),
	                addressDTO
	        );
	    }
	 
	 
	 private Employee mapToEntity(EmployeeRequestDTO dto) {

	        Addresses address = addressRepository.findById(dto.getAddressId())
	        		.orElseThrow(() -> new ResourceNotFoundException("Address", dto.getAddressId()));

	        Employee emp = new Employee();
	        emp.setFirstName(dto.getFirstName());
	        emp.setLastName(dto.getLastName());
	        emp.setPosition(dto.getPosition());
	        emp.setHireDate(dto.getHireDate());
	        emp.setPhoneNumber(dto.getPhoneNumber());
	        emp.setEmail(dto.getEmail());
	        emp.setAddress(address);

	        return emp;
	    }
   

    @Override
    public EmployeeResponseDTO getEmployeeById(Integer employeeId) {
    	   Employee emp = employeeRepository.findById(employeeId)
    			   .orElseThrow(() -> new ResourceNotFoundException("Employee", employeeId));
           return mapToDTO(emp);
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    
    @Override
    public  List<EmployeeResponseDTO> getEmployeesByPosition(String position){
    	return employeeRepository.findByPosition(position)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    	
    }

    @Override
    public SuccessDTO updateEmployee(int employeeId, EmployeeRequestDTO requestDTO) {

        Employee emp = employeeRepository.findById(employeeId)
        		.orElseThrow(() -> new ResourceNotFoundException("Employee", employeeId));

        Addresses address = addressRepository.findById(requestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", requestDTO.getAddressId()));

        emp.setFirstName(requestDTO.getFirstName());
        emp.setLastName(requestDTO.getLastName());
        emp.setPosition(requestDTO.getPosition());
        emp.setHireDate(requestDTO.getHireDate());
        emp.setPhoneNumber(requestDTO.getPhoneNumber());
        emp.setEmail(requestDTO.getEmail());
        emp.setAddress(address);
        
        employeeRepository.save(emp);

        return new SuccessDTO("Employee updated successfully");
    }

   
    
    @Override
    public SuccessDTO deleteEmployee(int employeeID) {
    	Employee emp = employeeRepository.findById(employeeID)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", employeeID));
    			employeeRepository.delete(emp);
    			return new SuccessDTO("Emplolyee deleted successfully");
    			
    	
    }
    
   
}