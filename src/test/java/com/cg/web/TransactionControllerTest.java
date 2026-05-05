package com.cg.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.cg.dto.CustomersResponseDTO;
import com.cg.dto.PetResponseDTO;
import com.cg.dto.TransactionRequestDTO;
import com.cg.dto.TransactionResponseDTO;
import com.cg.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private TransactionResponseDTO transactionResponseDTO;
    private TransactionRequestDTO transactionRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        CustomersResponseDTO customerDTO = new CustomersResponseDTO();
        customerDTO.setCustomerId(1);
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Doe");
        customerDTO.setEmail("johndoe@gmail.com");
        customerDTO.setPhoneNumber("9876543210");

        PetResponseDTO petDTO = new PetResponseDTO();
        petDTO.setPetId(10);
        petDTO.setName("Bruno");
        petDTO.setBreed("Labrador");
        petDTO.setAge(2);
        petDTO.setPrice(15000.0);

        transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setTransactionId(100);
        transactionResponseDTO.setTransactionDate(LocalDate.of(2026, 4, 17));
        transactionResponseDTO.setAmount(15000.0);
        transactionResponseDTO.setTransactionStatus("SUCCESS");
        transactionResponseDTO.setCustomers(customerDTO);
        transactionResponseDTO.setPet(petDTO);

        transactionRequestDTO = new TransactionRequestDTO();
        transactionRequestDTO.setTransactionDate(LocalDate.of(2026, 4, 17));
        transactionRequestDTO.setAmount(15000.0);
        transactionRequestDTO.setTransactionStatus("SUCCESS");
        transactionRequestDTO.setItemType("Pet");
        transactionRequestDTO.setItemName("Bruno");
        transactionRequestDTO.setQuantity(1);
        transactionRequestDTO.setCustomerId(1);
        transactionRequestDTO.setPetId(10);
    }

    @Test
    void testGetAll_success() throws Exception {
        List<TransactionResponseDTO> transactions = Arrays.asList(transactionResponseDTO);
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].transactionId").value(100))
                .andExpect(jsonPath("$[0].amount").value(15000.0))
                .andExpect(jsonPath("$[0].transactionStatus").value("SUCCESS"));
    }

    @Test
    void testGetById_success() throws Exception {
        when(transactionService.getTransactionById(100)).thenReturn(transactionResponseDTO);

        mockMvc.perform(get("/api/transactions/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(100))
                .andExpect(jsonPath("$.amount").value(15000.0))
                .andExpect(jsonPath("$.transactionStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.customers.customerId").value(1))
                .andExpect(jsonPath("$.customers.firstName").value("John"))
                .andExpect(jsonPath("$.pet.petId").value(10))
                .andExpect(jsonPath("$.pet.name").value("Bruno"));
    }

    @Test
    void testGetByStatus_success() throws Exception {
        List<TransactionResponseDTO> transactions = Arrays.asList(transactionResponseDTO);
        when(transactionService.getByStatus("SUCCESS")).thenReturn(transactions);

        mockMvc.perform(get("/api/transactions/status/SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].transactionId").value(100))
                .andExpect(jsonPath("$[0].transactionStatus").value("SUCCESS"));
    }

    @Test
    void testCreate_success() throws Exception {
        when(transactionService.addTransaction(any(TransactionRequestDTO.class)))
                .thenReturn(transactionResponseDTO);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value(100))
                .andExpect(jsonPath("$.amount").value(15000.0))
                .andExpect(jsonPath("$.transactionStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.customers.customerId").value(1))
                .andExpect(jsonPath("$.pet.petId").value(10));
    }

    @Test
    void testUpdateStatus_success() throws Exception {
        TransactionResponseDTO updatedResponse = new TransactionResponseDTO();
        updatedResponse.setTransactionId(100);
        updatedResponse.setTransactionDate(LocalDate.of(2026, 4, 17));
        updatedResponse.setAmount(15000.0);
        updatedResponse.setTransactionStatus("FAILED");

        when(transactionService.updateTransactionStatus(eq(100), eq("FAILED")))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/transactions/100/status")
                .param("status", "FAILED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(100))
                .andExpect(jsonPath("$.transactionStatus").value("FAILED"));
    }
}