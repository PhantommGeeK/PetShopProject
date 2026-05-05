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

import com.cg.dto.PetFoodRequestDTO;
import com.cg.dto.PetFoodResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.PetFoodService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class PetFoodControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetFoodService petFoodService;

    @InjectMocks
    private PetFoodController petFoodController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private PetFoodResponseDTO petFoodResponseDTO;
    private PetFoodRequestDTO petFoodRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petFoodController).build();

        petFoodResponseDTO = new PetFoodResponseDTO();
        petFoodResponseDTO.setFoodId(1);
        petFoodResponseDTO.setName("Royal Canin");
        petFoodResponseDTO.setBrand("Royal Canin");
        petFoodResponseDTO.setType("Dry");
        petFoodResponseDTO.setQuantity(10);
        petFoodResponseDTO.setPrice(1200.0);

        petFoodRequestDTO = new PetFoodRequestDTO();
        petFoodRequestDTO.setName("Royal Canin");
        petFoodRequestDTO.setBrand("Royal Canin");
        petFoodRequestDTO.setType("Dry");
        petFoodRequestDTO.setQuantity(10);
        petFoodRequestDTO.setPrice(1200.0);
    }

    @Test
    void testCreatePetFood_success() throws Exception {
        when(petFoodService.createPetFood(any(PetFoodRequestDTO.class)))
                .thenReturn(petFoodResponseDTO);

        mockMvc.perform(post("/api/pet-foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petFoodRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.foodId").value(1))
                .andExpect(jsonPath("$.name").value("Royal Canin"))
                .andExpect(jsonPath("$.type").value("Dry"))
                .andExpect(jsonPath("$.price").value(1200.0));
    }

    @Test
    void testGetAllPetFoods_success() throws Exception {
        List<PetFoodResponseDTO> foods = Arrays.asList(petFoodResponseDTO);
        when(petFoodService.getAllPetFoods()).thenReturn(foods);

        mockMvc.perform(get("/api/pet-foods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].foodId").value(1))
                .andExpect(jsonPath("$[0].name").value("Royal Canin"))
                .andExpect(jsonPath("$[0].brand").value("Royal Canin"))
                .andExpect(jsonPath("$[0].quantity").value(10));
    }

    @Test
    void testGetPetFoodById_success() throws Exception {
        when(petFoodService.getPetFoodById(1)).thenReturn(petFoodResponseDTO);

        mockMvc.perform(get("/api/pet-foods/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.foodId").value(1))
                .andExpect(jsonPath("$.name").value("Royal Canin"))
                .andExpect(jsonPath("$.brand").value("Royal Canin"))
                .andExpect(jsonPath("$.type").value("Dry"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.price").value(1200.0));
    }

    @Test
    void testGetPetFoodsByType_success() throws Exception {
        List<PetFoodResponseDTO> foods = Arrays.asList(petFoodResponseDTO);
        when(petFoodService.getPetFoodsByType("Dry")).thenReturn(foods);

        mockMvc.perform(get("/api/pet-foods/type/Dry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].foodId").value(1))
                .andExpect(jsonPath("$[0].type").value("Dry"));
    }

    @Test
    void testUpdatePetFood_success() throws Exception {
        PetFoodResponseDTO updatedResponse = new PetFoodResponseDTO();
        updatedResponse.setFoodId(1);
        updatedResponse.setName("Royal Canin Updated");
        updatedResponse.setBrand("Royal Canin");
        updatedResponse.setType("Wet");
        updatedResponse.setQuantity(20);
        updatedResponse.setPrice(1500.0);

        when(petFoodService.updatePetFood(eq(1), any(PetFoodRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/pet-foods/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petFoodRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.foodId").value(1))
                .andExpect(jsonPath("$.name").value("Royal Canin Updated"))
                .andExpect(jsonPath("$.type").value("Wet"))
                .andExpect(jsonPath("$.price").value(1500.0));
    }

    @Test
    void testDeletePetFood_success() throws Exception {
        when(petFoodService.deletePetFood(1))
                .thenReturn(new SuccessDTO("Pet food deleted successfully"));

        mockMvc.perform(delete("/api/pet-foods/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pet food deleted successfully"));
    }
}