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

import com.cg.dto.PetCategoryRequestDTO;
import com.cg.dto.PetCategoryResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.PetCategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class PetCategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetCategoryService petCategoryService;

    @InjectMocks
    private PetCategoryController petCategoryController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private PetCategoryResponseDTO categoryResponseDTO;
    private PetCategoryRequestDTO categoryRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petCategoryController).build();

        categoryResponseDTO = new PetCategoryResponseDTO();
        categoryResponseDTO.setCategoryId(1);
        categoryResponseDTO.setName("Dogs");

        categoryRequestDTO = new PetCategoryRequestDTO();
        categoryRequestDTO.setName("Dogs");
    }

    @Test
    void testCreateCategory_success() throws Exception {
        when(petCategoryService.createCategory(any(PetCategoryRequestDTO.class)))
                .thenReturn(categoryResponseDTO);

        mockMvc.perform(post("/api/pet-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.name").value("Dogs"));
    }

    @Test
    void testGetAllCategories_success() throws Exception {
        PetCategoryResponseDTO category2 = new PetCategoryResponseDTO();
        category2.setCategoryId(2);
        category2.setName("Cats");

        List<PetCategoryResponseDTO> categories = Arrays.asList(categoryResponseDTO, category2);
        when(petCategoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/pet-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].categoryId").value(1))
                .andExpect(jsonPath("$[0].name").value("Dogs"))
                .andExpect(jsonPath("$[1].categoryId").value(2))
                .andExpect(jsonPath("$[1].name").value("Cats"));
    }

    @Test
    void testGetCategoryById_success() throws Exception {
        when(petCategoryService.getCategoryById(1)).thenReturn(categoryResponseDTO);

        mockMvc.perform(get("/api/pet-categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.name").value("Dogs"));
    }

    @Test
    void testUpdateCategory_success() throws Exception {
        PetCategoryResponseDTO updatedResponse = new PetCategoryResponseDTO();
        updatedResponse.setCategoryId(1);
        updatedResponse.setName("Dogs Updated");

        when(petCategoryService.updateCategory(eq(1), any(PetCategoryRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/pet-categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.name").value("Dogs Updated"));
    }

    @Test
    void testDeleteCategory_success() throws Exception {
        when(petCategoryService.deleteCategory(1))
                .thenReturn(new SuccessDTO("Category deleted successfully"));

        mockMvc.perform(delete("/api/pet-categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category deleted successfully"));
    }
}