package com.cg.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cg.dto.AddressesResponseDTO;
import com.cg.dto.EmployeeRequestDTO;
import com.cg.dto.EmployeeResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private EmployeeResponseDTO employeeResponseDTO;
    private EmployeeRequestDTO employeeRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        AddressesResponseDTO addressDTO = new AddressesResponseDTO();
        addressDTO.setAddressId(1);
        addressDTO.setStreet("MG Road");
        addressDTO.setCity("Delhi");
        addressDTO.setState("UP");
        addressDTO.setZipCode("201001");

        employeeResponseDTO = new EmployeeResponseDTO();
        employeeResponseDTO.setEmployeeId(1);
        employeeResponseDTO.setFirstName("Amit");
        employeeResponseDTO.setLastName("Singh");
        employeeResponseDTO.setPosition("Groomer");
        employeeResponseDTO.setHireDate(LocalDate.of(2024, 1, 10));
        employeeResponseDTO.setPhoneNumber("9876543211");
        employeeResponseDTO.setEmail("amit@gmail.com");
        employeeResponseDTO.setAddress(addressDTO);

        employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFirstName("Amit");
        employeeRequestDTO.setLastName("Singh");
        employeeRequestDTO.setPosition("Groomer");
        employeeRequestDTO.setHireDate(LocalDate.of(2024, 1, 10));
        employeeRequestDTO.setPhoneNumber("9876543211");
        employeeRequestDTO.setEmail("amit@gmail.com");
        employeeRequestDTO.setAddressId(1);
    }

    @Test
    void testGetAllEmployees_success() throws Exception {
        List<EmployeeResponseDTO> employees = Arrays.asList(employeeResponseDTO);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].employeeId").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Amit"))
                .andExpect(jsonPath("$[0].lastName").value("Singh"))
                .andExpect(jsonPath("$[0].position").value("Groomer"));
    }

    @Test
    void testGetEmployeeById_success() throws Exception {
        when(employeeService.getEmployeeById(1)).thenReturn(employeeResponseDTO);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1))
                .andExpect(jsonPath("$.firstName").value("Amit"))
                .andExpect(jsonPath("$.lastName").value("Singh"))
                .andExpect(jsonPath("$.position").value("Groomer"))
                .andExpect(jsonPath("$.email").value("amit@gmail.com"))
                .andExpect(jsonPath("$.address.city").value("Delhi"));
    }

    @Test
    void testUpdateEmployee_success() throws Exception {
        when(employeeService.updateEmployee(eq(1), any(EmployeeRequestDTO.class)))
                .thenReturn(new SuccessDTO("Employee updated successfully"));

        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee updated successfully"));
    }

    @Test
    void testDeleteEmployee_success() throws Exception {
        when(employeeService.deleteEmployee(1))
                .thenReturn(new SuccessDTO("Employee deleted successfully"));

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
    }
}