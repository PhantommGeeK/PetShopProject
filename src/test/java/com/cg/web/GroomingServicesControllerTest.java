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

import com.cg.dto.GroomingServicesRequestDTO;
import com.cg.dto.GroomingServicesResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.GroomingServicesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class GroomingServicesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroomingServicesService service;

    @InjectMocks
    private GroomingServicesController groomingServicesController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private GroomingServicesResponseDTO groomingResponseDTO;
    private GroomingServicesRequestDTO groomingRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groomingServicesController).build();

        groomingResponseDTO = new GroomingServicesResponseDTO();
        groomingResponseDTO.setServiceId(1);
        groomingResponseDTO.setName("Full Grooming");
        groomingResponseDTO.setDescription("Complete bath and haircut");
        groomingResponseDTO.setPrice(800.0);
        groomingResponseDTO.setAvailable(true);

        groomingRequestDTO = new GroomingServicesRequestDTO();
        groomingRequestDTO.setName("Full Grooming");
        groomingRequestDTO.setDescription("Complete bath and haircut");
        groomingRequestDTO.setPrice(800.0);
        groomingRequestDTO.setAvailable(true);
    }

    @Test
    void testGetAllServices_success() throws Exception {
        List<GroomingServicesResponseDTO> services = Arrays.asList(groomingResponseDTO);
        when(service.getAllServices()).thenReturn(services);

        mockMvc.perform(get("/api/grooming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].serviceId").value(1))
                .andExpect(jsonPath("$[0].name").value("Full Grooming"))
                .andExpect(jsonPath("$[0].price").value(800.0))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void testGetServiceById_success() throws Exception {
        when(service.getServiceById(1)).thenReturn(groomingResponseDTO);

        mockMvc.perform(get("/api/grooming/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").value(1))
                .andExpect(jsonPath("$.name").value("Full Grooming"))
                .andExpect(jsonPath("$.description").value("Complete bath and haircut"))
                .andExpect(jsonPath("$.price").value(800.0))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testGetAvailableServices_success() throws Exception {
        List<GroomingServicesResponseDTO> services = Arrays.asList(groomingResponseDTO);
        when(service.getAvailableServices()).thenReturn(services);

        mockMvc.perform(get("/api/grooming/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].serviceId").value(1))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void testCreateService_success() throws Exception {
        when(service.createService(any(GroomingServicesRequestDTO.class)))
                .thenReturn(new SuccessDTO("Grooming service created successfully"));

        mockMvc.perform(post("/api/grooming")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groomingRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message")
                        .value("Grooming service created successfully"));
    }

    @Test
    void testUpdateService_success() throws Exception {
        when(service.updateService(eq(1), any(GroomingServicesRequestDTO.class)))
                .thenReturn(new SuccessDTO("Grooming service updated successfully"));

        mockMvc.perform(put("/api/grooming/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(groomingRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Grooming service updated successfully"));
    }

    @Test
    void testDeleteService_success() throws Exception {
        when(service.deleteService(1))
                .thenReturn(new SuccessDTO("Grooming service deleted successfully"));

        mockMvc.perform(delete("/api/grooming/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Grooming service deleted successfully"));
    }
}