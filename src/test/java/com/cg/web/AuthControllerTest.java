package com.cg.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cg.dto.CustomerRegisterDTO;
import com.cg.dto.EmployeeRegisterDTO;
import com.cg.dto.LoginRequestDTO;
import com.cg.dto.LoginResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRegisterDTO;
import com.cg.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private CustomerRegisterDTO customerRegisterDTO;
    private SupplierRegisterDTO supplierRegisterDTO;
    private EmployeeRegisterDTO employeeRegisterDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        customerRegisterDTO = new CustomerRegisterDTO();
        customerRegisterDTO.setUsername("johndoe");
        customerRegisterDTO.setPassword("password123");
        customerRegisterDTO.setFirstName("John");
        customerRegisterDTO.setLastName("Doe");
        customerRegisterDTO.setEmail("johndoe@gmail.com");
        customerRegisterDTO.setPhoneNumber("9876543210");

        supplierRegisterDTO = new SupplierRegisterDTO();
        supplierRegisterDTO.setUsername("supplier1");
        supplierRegisterDTO.setPassword("password123");
        supplierRegisterDTO.setName("Pet Supplies Co");
        supplierRegisterDTO.setContactPerson("Rahul Kumar");
        supplierRegisterDTO.setPhoneNumber("9876543210");
        supplierRegisterDTO.setEmail("supplier@gmail.com");

        employeeRegisterDTO = new EmployeeRegisterDTO();
        employeeRegisterDTO.setUsername("emp1");
        employeeRegisterDTO.setPassword("password123");
        employeeRegisterDTO.setFirstName("Amit");
        employeeRegisterDTO.setLastName("Singh");
        employeeRegisterDTO.setPosition("Groomer");
        employeeRegisterDTO.setHireDate(java.time.LocalDate.of(2024, 1, 10));
        employeeRegisterDTO.setPhoneNumber("9876543211");
        employeeRegisterDTO.setEmail("amit@gmail.com");

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("johndoe");
        loginRequestDTO.setPassword("password123");
    }

    @Test
    void testRegisterCustomer_success() throws Exception {
        when(authService.registerCustomer(any(CustomerRegisterDTO.class)))
                .thenReturn(new SuccessDTO("Customer registered successfully"));

        mockMvc.perform(post("/auth/register/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRegisterDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Customer registered successfully"));
    }

    @Test
    void testRegisterSupplier_success() throws Exception {
        when(authService.registerSupplier(any(SupplierRegisterDTO.class)))
                .thenReturn(new SuccessDTO("Supplier registered successfully"));

        mockMvc.perform(post("/auth/register/supplier")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierRegisterDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Supplier registered successfully"));
    }

    @Test
    void testRegisterEmployee_success() throws Exception {
        when(authService.registerEmployee(any(EmployeeRegisterDTO.class)))
                .thenReturn(new SuccessDTO("Employee registered successfully"));

        mockMvc.perform(post("/auth/register/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRegisterDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Employee registered successfully"));
    }

    @Test
    void testLogin_success() throws Exception {
        when(authService.login(any(LoginRequestDTO.class)))
                .thenReturn(new LoginResponseDTO("mock-jwt-token"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }
}