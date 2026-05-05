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

import com.cg.dto.PetCategoryResponseDTO;
import com.cg.dto.PetRequestDTO;
import com.cg.dto.PetResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private PetResponseDTO petResponseDTO;
    private PetRequestDTO petRequestDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();

        PetCategoryResponseDTO categoryDTO = new PetCategoryResponseDTO();
        categoryDTO.setCategoryId(1);
        categoryDTO.setName("Dogs");

        petResponseDTO = new PetResponseDTO();
        petResponseDTO.setPetId(1);
        petResponseDTO.setName("Bruno");
        petResponseDTO.setBreed("Labrador");
        petResponseDTO.setAge(2);
        petResponseDTO.setPrice(15000.0);
        petResponseDTO.setCategory(categoryDTO);
        petResponseDTO.setDescription("Friendly dog");
        petResponseDTO.setImageUrl("http://example.com/bruno.jpg");

        petRequestDTO = new PetRequestDTO();
        petRequestDTO.setName("Bruno");
        petRequestDTO.setBreed("Labrador");
        petRequestDTO.setAge(2);
        petRequestDTO.setPrice(15000.0);
        petRequestDTO.setCategoryId(1);
        petRequestDTO.setDescription("Friendly dog");
        petRequestDTO.setImageUrl("http://example.com/bruno.jpg");
    }

    @Test
    void testAddPet_success() throws Exception {
        when(petService.addPet(any(PetRequestDTO.class)))
                .thenReturn(new SuccessDTO("Pet added successfully"));

        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Pet added successfully"));
    }

    @Test
    void testGetAllPets_success() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponseDTO);
        when(petService.getAllPets()).thenReturn(pets);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].petId").value(1))
                .andExpect(jsonPath("$[0].name").value("Bruno"))
                .andExpect(jsonPath("$[0].breed").value("Labrador"))
                .andExpect(jsonPath("$[0].price").value(15000.0));
    }

    @Test
    void testGetPetById_success() throws Exception {
        when(petService.getPetById(1)).thenReturn(petResponseDTO);

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petId").value(1))
                .andExpect(jsonPath("$.name").value("Bruno"))
                .andExpect(jsonPath("$.breed").value("Labrador"))
                .andExpect(jsonPath("$.age").value(2))
                .andExpect(jsonPath("$.price").value(15000.0))
                .andExpect(jsonPath("$.category.name").value("Dogs"));
    }

    @Test
    void testGetPetsByCategory_success() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponseDTO);
        when(petService.getPetsByCategory(1)).thenReturn(pets);

        mockMvc.perform(get("/api/pets/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].petId").value(1))
                .andExpect(jsonPath("$[0].category.categoryId").value(1));
    }

    @Test
    void testUpdatePet_success() throws Exception {
        when(petService.updatePet(eq(1), any(PetRequestDTO.class)))
                .thenReturn(new SuccessDTO("Pet updated successfully"));

        mockMvc.perform(put("/api/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pet updated successfully"));
    }

    @Test
    void testDeletePet_success() throws Exception {
        when(petService.deletePet(1))
                .thenReturn(new SuccessDTO("Pet deleted successfully"));

        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Pet deleted successfully"));
    }

    @Test
    void testSearchByName_success() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponseDTO);
        when(petService.searchPetsByName("Bruno")).thenReturn(pets);

        mockMvc.perform(get("/api/pets/search").param("name", "Bruno"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Bruno"));
    }

    @Test
    void testSearchByAge_success() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponseDTO);
        when(petService.getPetsByAge(2)).thenReturn(pets);

        mockMvc.perform(get("/api/pets/search").param("age", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].age").value(2));
    }

    @Test
    void testSearchByBreed_success() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponseDTO);
        when(petService.searchPetsByBreed("Labrador")).thenReturn(pets);

        mockMvc.perform(get("/api/pets/search").param("breed", "Labrador"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].breed").value("Labrador"));
    }

    @Test
    void testSearchByPriceRange_success() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponseDTO);
        when(petService.getPetsByPriceRange(5000.0, 20000.0)).thenReturn(pets);

        mockMvc.perform(get("/api/pets/search")
                .param("minPrice", "5000.0")
                .param("maxPrice", "20000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].price").value(15000.0));
    }
}