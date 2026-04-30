package com.cg.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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

import com.cg.dto.VaccinationRequestDTO;
import com.cg.dto.VaccinationResponseDTO;
import com.cg.service.VaccinationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class VaccinationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VaccinationService vaccinationService;

    @InjectMocks
    private VaccinationController vaccinationController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private VaccinationResponseDTO vaccinationResponseDTO;
    private VaccinationRequestDTO vaccinationRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vaccinationController).build();

        vaccinationResponseDTO = new VaccinationResponseDTO();
        vaccinationResponseDTO.setVaccinationId(1);
        vaccinationResponseDTO.setName("Rabies Vaccine");
        vaccinationResponseDTO.setDescription("Annual rabies vaccination");
        vaccinationResponseDTO.setPrice(500.0);
        vaccinationResponseDTO.setAvailable(true);

        vaccinationRequestDTO = new VaccinationRequestDTO();
        vaccinationRequestDTO.setName("Rabies Vaccine");
        vaccinationRequestDTO.setDescription("Annual rabies vaccination");
        vaccinationRequestDTO.setPrice(500.0);
        vaccinationRequestDTO.setAvailable(true);
    }

    @Test
    void testGetAll_success() throws Exception {
        List<VaccinationResponseDTO> vaccinations = Arrays.asList(vaccinationResponseDTO);
        when(vaccinationService.getAllVaccinations()).thenReturn(vaccinations);

        mockMvc.perform(get("/api/vaccinations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].vaccinationId").value(1))
                .andExpect(jsonPath("$[0].name").value("Rabies Vaccine"))
                .andExpect(jsonPath("$[0].price").value(500.0))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void testGetById_success() throws Exception {
        when(vaccinationService.getVaccinationById(1)).thenReturn(vaccinationResponseDTO);

        mockMvc.perform(get("/api/vaccinations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vaccinationId").value(1))
                .andExpect(jsonPath("$.name").value("Rabies Vaccine"))
                .andExpect(jsonPath("$.description").value("Annual rabies vaccination"))
                .andExpect(jsonPath("$.price").value(500.0))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testGetAvailable_success() throws Exception {
        List<VaccinationResponseDTO> available = Arrays.asList(vaccinationResponseDTO);
        when(vaccinationService.getAvailableVaccinations()).thenReturn(available);

        mockMvc.perform(get("/api/vaccinations/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].vaccinationId").value(1))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void testAdd_success() throws Exception {
        when(vaccinationService.addVaccination(any(VaccinationRequestDTO.class)))
                .thenReturn(vaccinationResponseDTO);

        mockMvc.perform(post("/api/vaccinations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vaccinationRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vaccinationId").value(1))
                .andExpect(jsonPath("$.name").value("Rabies Vaccine"))
                .andExpect(jsonPath("$.price").value(500.0))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void testUpdate_success() throws Exception {
        VaccinationResponseDTO updatedResponse = new VaccinationResponseDTO();
        updatedResponse.setVaccinationId(1);
        updatedResponse.setName("Rabies Vaccine Updated");
        updatedResponse.setDescription("Updated description");
        updatedResponse.setPrice(600.0);
        updatedResponse.setAvailable(true);

        when(vaccinationService.updateVaccination(eq(1), any(VaccinationRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/vaccinations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vaccinationRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vaccinationId").value(1))
                .andExpect(jsonPath("$.name").value("Rabies Vaccine Updated"))
                .andExpect(jsonPath("$.price").value(600.0));
    }

    @Test
    void testDelete_success() throws Exception {
        doNothing().when(vaccinationService).deleteVaccination(1);

        mockMvc.perform(delete("/api/vaccinations/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Vaccination deleted successfully!"));
    }
}