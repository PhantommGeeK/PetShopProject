package com.cg.service;
import com.cg.dto.AddressesResponseDTO;
import com.cg.dto.EmployeeRequestDTO;
import com.cg.dto.EmployeeResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.Addresses;
import com.cg.entity.Employee;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressesRepository;
import com.cg.repo.EmployeeRepository;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	
	@Mock
    private EmployeeRepository employeeRepository;
 
    @Mock
    private AddressesRepository addressRepository;
    
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    
    private Addresses address;
    private Employee employee;
    private EmployeeRequestDTO requestDTO;
    
    @BeforeEach
    void setUp() {
    	
    	address = new Addresses();
        address.setAddressId(1);
        address.setStreet("123 Main St");
        address.setCity("Hyderabad");
        address.setState("Telangana");
        address.setZipCode("500001");
        
        employee = new Employee();
        employee.setEmployeeId(1);
        employee.setFirstName("Arjun");
        employee.setLastName("Sharma");
        employee.setPosition("Developer");
        employee.setHireDate(LocalDate.of(2023, 1, 15));
        employee.setPhoneNumber("9876543210");
        employee.setEmail("arjun@example.com");
        employee.setAddress(address);
        
        requestDTO = new EmployeeRequestDTO();
        requestDTO.setFirstName("Arjun");
        requestDTO.setLastName("Sharma");
        requestDTO.setPosition("Developer");
        requestDTO.setHireDate(LocalDate.of(2023, 1, 15));
        requestDTO.setPhoneNumber("9876543210");
        requestDTO.setEmail("arjun@example.com");
        requestDTO.setAddressId(1);
        
        
    }
    
    @Test
    void getAllEmployees_ShouldReturnEmptyList_WhenNoEmployeesExist() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        List<EmployeeResponseDTO> result = employeeService.getAllEmployees();
        assertTrue(result.isEmpty());
    }
    
    @Test
    void getEmployeesByPosition_ShouldReturnMatching_WhenPositionExists() {
       
        when(employeeRepository.findByPosition("Developer"))
                .thenReturn(List.of(employee));
        List<EmployeeResponseDTO> result = employeeService.getEmployeesByPosition("Developer");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Developer", result.get(0).getPosition());
    }
    
    @Test
    void getEmployeesByPosition_ShouldReturnEmptyList_WhenNoMatchFound() {
        
        when(employeeRepository.findByPosition("CEO"))
                .thenReturn(Collections.emptyList());
        List<EmployeeResponseDTO> result = employeeService.getEmployeesByPosition("CEO");
        assertTrue(result.isEmpty());
    }
    
    @Test
    void updateEmployee_ShouldReturnSuccess_WhenEmployeeAndAddressExist() {
     
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
 
       
        requestDTO.setFirstName("Arjun Updated");
        requestDTO.setPosition("Senior Developer");
 
       
        SuccessDTO result = employeeService.updateEmployee(1, requestDTO);
        assertNotNull(result);
        assertEquals("Employee updated successfully", result.getMessage());
        assertEquals("Arjun Updated",     employee.getFirstName());
        assertEquals("Senior Developer",  employee.getPosition());
 
        verify(employeeRepository, times(1)).save(employee);
    }
    
    @Test
    void updateEmployee_ShouldThrowException_WhenEmployeeNotFound() {
       
        when(employeeRepository.findById(99)).thenReturn(Optional.empty());
 
       
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(99, requestDTO)
        );
 
        assertEquals("Employee not found", ex.getMessage());
        verify(employeeRepository, never()).save(any());
    }
 
    @Test
    void updateEmployee_ShouldThrowException_WhenAddressNotFound() {
       
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(addressRepository.findById(1)).thenReturn(Optional.empty());
 
      
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(1, requestDTO)
        );
 
        assertEquals("Address not found", ex.getMessage());
        verify(employeeRepository, never()).save(any());
    }
 
 
   
    @Test
    void deleteEmployee_ShouldReturnSuccess_WhenEmployeeExists() {
       
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(employee);  
 
       
        SuccessDTO result = employeeService.deleteEmployee(1);
 
        
        assertNotNull(result);
        assertEquals("Emplolyee deleted successfully", result.getMessage());
 

        verify(employeeRepository, times(1)).delete(employee);
    }
 
    @Test
    void deleteEmployee_ShouldThrowException_WhenEmployeeNotFound() {
        
        when(employeeRepository.findById(99)).thenReturn(Optional.empty());
 
       
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(99)
        );
 
        assertEquals("Employee not found", ex.getMessage());
 
        
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

}
