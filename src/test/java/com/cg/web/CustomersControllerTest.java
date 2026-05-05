package com.cg.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.cg.dto.CustomerTransactionSummaryDTO;
import com.cg.dto.CustomersRequestDTO;
import com.cg.dto.CustomersResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.CustomersService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class CustomersControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomersService customersService;

    @InjectMocks
    private CustomersController customersController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private CustomersResponseDTO customerResponseDTO;
    private CustomersRequestDTO customerRequestDTO;
    private AddressesResponseDTO addressResponseDTO;
    private CustomerTransactionSummaryDTO transactionSummaryDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customersController).build();

        addressResponseDTO = new AddressesResponseDTO();
        addressResponseDTO.setAddressId(1);
        addressResponseDTO.setStreet("MG Road");
        addressResponseDTO.setCity("Delhi");
        addressResponseDTO.setState("UP");
        addressResponseDTO.setZipCode("201001");

        customerResponseDTO = new CustomersResponseDTO();
        customerResponseDTO.setCustomerId(1);
        customerResponseDTO.setFirstName("John");
        customerResponseDTO.setLastName("Doe");
        customerResponseDTO.setEmail("johndoe@gmail.com");
        customerResponseDTO.setPhoneNumber("9876543210");
        customerResponseDTO.setAddress(addressResponseDTO);

        customerRequestDTO = new CustomersRequestDTO();
        customerRequestDTO.setFirstName("John");
        customerRequestDTO.setLastName("Doe");
        customerRequestDTO.setEmail("johndoe@gmail.com");
        customerRequestDTO.setPhoneNumber("9876543210");
        customerRequestDTO.setAddressId(1);

        transactionSummaryDTO = new CustomerTransactionSummaryDTO();
        transactionSummaryDTO.setCustomerId(1);
        transactionSummaryDTO.setFirstName("John");
        transactionSummaryDTO.setLastName("Doe");
        transactionSummaryDTO.setTransactionIds(Arrays.asList(101, 102, 103));
        transactionSummaryDTO.setTotalTransactions(3L);
        transactionSummaryDTO.setSuccessfulPurchases(2L);
    }

    @Test
    void testGetAllCustomers_success() throws Exception {
        List<CustomersResponseDTO> customers = Arrays.asList(customerResponseDTO);
        when(customersService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("johndoe@gmail.com"));
    }

    @Test
    void testGetCustomerById_success() throws Exception {
        when(customersService.getCustomerById(1)).thenReturn(customerResponseDTO);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("9876543210"))
                .andExpect(jsonPath("$.address.street").value("MG Road"))
                .andExpect(jsonPath("$.address.city").value("Delhi"));
    }

    @Test
    void testGetCustomerByEmail_success() throws Exception {
        when(customersService.getCustomerByEmail("johndoe@gmail.com"))
                .thenReturn(customerResponseDTO);

        mockMvc.perform(get("/api/customers/email/johndoe@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("johndoe@gmail.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetCustomerTransactions_success() throws Exception {
        when(customersService.getCustomerTransactionSummary(1))
                .thenReturn(transactionSummaryDTO);

        mockMvc.perform(get("/api/customers/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.totalTransactions").value(3))
                .andExpect(jsonPath("$.successfulPurchases").value(2))
                .andExpect(jsonPath("$.transactionIds.length()").value(3));
    }

    @Test
    void testGetCustomersByCity_success() throws Exception {
        List<CustomersResponseDTO> customers = Arrays.asList(customerResponseDTO);
        when(customersService.getCustomersByCity("Delhi")).thenReturn(customers);

        mockMvc.perform(get("/api/customers/city/Delhi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].address.city").value("Delhi"));
    }

    @Test
    void testUpdateCustomer_success() throws Exception {
        CustomersResponseDTO updatedResponse = new CustomersResponseDTO();
        updatedResponse.setCustomerId(1);
        updatedResponse.setFirstName("John Updated");
        updatedResponse.setLastName("Doe");
        updatedResponse.setEmail("johndoe@gmail.com");
        updatedResponse.setPhoneNumber("9876543210");
        updatedResponse.setAddress(addressResponseDTO);

        when(customersService.updateCustomer(eq(1), any(CustomersRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.firstName").value("John Updated"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@gmail.com"));
    }

    @Test
    void testDeleteCustomer_success() throws Exception {
        when(customersService.deleteCustomer(1))
                .thenReturn(new SuccessDTO("Customer with ID 1 deleted successfully"));

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Customer with ID 1 deleted successfully"));
    }
}