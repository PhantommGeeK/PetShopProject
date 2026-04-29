package com.cg.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.cg.dto.PetFoodRequestDTO;
import com.cg.dto.PetFoodResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.PetFood;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PetFoodRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PetFoodServiceTest {

    @Mock
    private PetFoodRepository petFoodRepository;

    @InjectMocks
    private PetFoodServiceImpl petFoodService;

    private PetFood petFood;

    @BeforeEach
    void setUp() {
        petFood = new PetFood();
        petFood.setFoodId(1);
        petFood.setName("Dog Food");
        petFood.setBrand("Pedigree");
        petFood.setType("Dry");
        petFood.setQuantity(10);
        petFood.setPrice(500.0);
    }

    @Test
    void testCreatePetFood_success() {
        PetFoodRequestDTO request = new PetFoodRequestDTO();
        request.setName("Dog Food");
        request.setBrand("Pedigree");
        request.setType("Dry");
        request.setQuantity(10);
        request.setPrice(500.0);

        when(petFoodRepository.save(any(PetFood.class))).thenReturn(petFood);

        PetFoodResponseDTO response = petFoodService.createPetFood(request);

        assertNotNull(response);
        assertEquals("Dog Food", response.getName());
        verify(petFoodRepository, times(1)).save(any(PetFood.class));
    }

    @Test
    void testGetPetFoodById_success() {
        when(petFoodRepository.findById(1)).thenReturn(Optional.of(petFood));

        PetFoodResponseDTO response = petFoodService.getPetFoodById(1);

        assertEquals("Dog Food", response.getName());
    }

    @Test
    void testGetPetFoodById_notFound() {
        when(petFoodRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            petFoodService.getPetFoodById(1);
        });
    }

    @Test
    void testGetAllPetFoods_success() {
        when(petFoodRepository.findAll()).thenReturn(Arrays.asList(petFood));

        List<PetFoodResponseDTO> result = petFoodService.getAllPetFoods();

        assertEquals(1, result.size());
    }

    @Test
    void testGetPetFoodsByType_success() {
        when(petFoodRepository.findAll()).thenReturn(Arrays.asList(petFood));

        List<PetFoodResponseDTO> result = petFoodService.getPetFoodsByType("Dry");

        assertEquals(1, result.size());
        assertEquals("Dry", result.get(0).getType());
    }

    @Test
    void testGetPetFoodsByType_noMatch() {
        when(petFoodRepository.findAll()).thenReturn(Arrays.asList(petFood));

        List<PetFoodResponseDTO> result = petFoodService.getPetFoodsByType("Wet");

        assertEquals(0, result.size());
    }

    @Test
    void testUpdatePetFood_success() {
        PetFoodRequestDTO request = new PetFoodRequestDTO();
        request.setName("Cat Food");
        request.setBrand("Whiskas");
        request.setType("Wet");
        request.setQuantity(5);
        request.setPrice(300.0);

        when(petFoodRepository.findById(1)).thenReturn(Optional.of(petFood));
        when(petFoodRepository.save(any(PetFood.class))).thenReturn(petFood);

        PetFoodResponseDTO response = petFoodService.updatePetFood(1, request);

        assertEquals("Cat Food", response.getName());
        assertEquals("Wet", response.getType());
    }

    @Test
    void testUpdatePetFood_notFound() {
        when(petFoodRepository.findById(1)).thenReturn(Optional.empty());

        PetFoodRequestDTO request = new PetFoodRequestDTO();

        assertThrows(ResourceNotFoundException.class, () -> {
            petFoodService.updatePetFood(1, request);
        });
    }

    @Test
    void testDeletePetFood_success() {
        when(petFoodRepository.findById(1)).thenReturn(Optional.of(petFood));
        doNothing().when(petFoodRepository).delete(petFood);

        SuccessDTO response = petFoodService.deletePetFood(1);

        assertTrue(response.getMessage().contains("deleted successfully"));
        verify(petFoodRepository, times(1)).delete(petFood);
    }

    @Test
    void testDeletePetFood_notFound() {
        when(petFoodRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            petFoodService.deletePetFood(1);
        });
    }
}