package com.cg.service;

import com.cg.dto.PetRequestDTO;
import com.cg.dto.PetResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.Pet;
import com.cg.entity.PetCategory;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PetCategoryRepository;
import com.cg.repo.PetRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetCategoryRepository categoryRepository;

    @InjectMocks
    private PetServiceImpl petService;

    private Pet pet;
    private PetCategory category;
    private PetRequestDTO requestDTO;
    private PetResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        category = new PetCategory();
        category.setCategoryId(1);
        category.setName("Dogs");

        pet = new Pet();
        pet.setPetId(1);
        pet.setName("Bruno");
        pet.setBreed("Labrador");
        pet.setAge(2);
        pet.setPrice(15000.0);
        pet.setDescription("Friendly dog");
        pet.setImageUrl("bruno.jpg");
        pet.setPetCategory(category);

        requestDTO = new PetRequestDTO();
        requestDTO.setName("Bruno");
        requestDTO.setBreed("Labrador");
        requestDTO.setAge(2);
        requestDTO.setPrice(15000.0);
        requestDTO.setDescription("Friendly dog");
        requestDTO.setImageUrl("bruno.jpg");
        requestDTO.setCategoryId(1);
    }


    

    @Test
    void addPet_ShouldReturnSuccess_WhenValidRequest() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        SuccessDTO result = petService.addPet(requestDTO);

        assertNotNull(result);
        assertEquals("Pet created successfully", result.getMessage());
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void addPet_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.addPet(requestDTO)
        );

        assertEquals("Pet category not found!", ex.getMessage());
        verify(petRepository, never()).save(any());
    }


    
    @Test
    void getAllPets_ShouldReturnList_WhenPetsExist() {
        try (MockedStatic<PetResponseDTO> mocked = mockStatic(PetResponseDTO.class)) {
            mocked.when(() -> PetResponseDTO.fromEntity(pet)).thenReturn(new PetResponseDTO());
            when(petRepository.findAll()).thenReturn(Arrays.asList(pet));

            List<PetResponseDTO> result = petService.getAllPets();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void getAllPets_ShouldThrowException_WhenNoPetsExist() {
        when(petRepository.findAll()).thenReturn(Collections.emptyList());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.getAllPets()
        );

        assertEquals("No pets found!", ex.getMessage());
    }


   

    @Test
    void getPetById_ShouldReturnPet_WhenPetExists() {
        try (MockedStatic<PetResponseDTO> mocked = mockStatic(PetResponseDTO.class)) {
            PetResponseDTO mockResponse = new PetResponseDTO();
            mocked.when(() -> PetResponseDTO.fromEntity(pet)).thenReturn(mockResponse);
            when(petRepository.findById(1)).thenReturn(Optional.of(pet));

            PetResponseDTO result = petService.getPetById(1);

            assertNotNull(result);
        }
    }

    @Test
    void getPetById_ShouldThrowException_WhenPetNotFound() {
        when(petRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.getPetById(99)
        );

        assertEquals("Pet not found", ex.getMessage());
    }


    @Test
    void getPetsByCategory_ShouldReturnList_WhenPetsExist() {
        try (MockedStatic<PetResponseDTO> mocked = mockStatic(PetResponseDTO.class)) {
            mocked.when(() -> PetResponseDTO.fromEntity(pet)).thenReturn(new PetResponseDTO());
            when(petRepository.findByPetCategoryCategoryId(1)).thenReturn(List.of(pet));

            List<PetResponseDTO> result = petService.getPetsByCategory(1);

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void getPetsByCategory_ShouldReturnEmptyList_WhenNoPetsInCategory() {
        when(petRepository.findByPetCategoryCategoryId(99)).thenReturn(Collections.emptyList());

        List<PetResponseDTO> result = petService.getPetsByCategory(99);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    

    @Test
    void getPetsByAge_ShouldReturnList_WhenPetsExist() {
        try (MockedStatic<PetResponseDTO> mocked = mockStatic(PetResponseDTO.class)) {
            mocked.when(() -> PetResponseDTO.fromEntity(pet)).thenReturn(new PetResponseDTO());
            when(petRepository.findByAge(2)).thenReturn(List.of(pet));

            List<PetResponseDTO> result = petService.getPetsByAge(2);

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void getPetsByAge_ShouldReturnEmptyList_WhenNoPetsFound() {
        when(petRepository.findByAge(10)).thenReturn(Collections.emptyList());

        List<PetResponseDTO> result = petService.getPetsByAge(10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    

    @Test
    void getPetsByPriceRange_ShouldReturnList_WhenPetsExist() {
        try (MockedStatic<PetResponseDTO> mocked = mockStatic(PetResponseDTO.class)) {
            mocked.when(() -> PetResponseDTO.fromEntity(pet)).thenReturn(new PetResponseDTO());
            when(petRepository.findByPriceBetween(5000.0, 20000.0)).thenReturn(List.of(pet));

            List<PetResponseDTO> result = petService.getPetsByPriceRange(5000.0, 20000.0);

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void getPetsByPriceRange_ShouldReturnEmptyList_WhenNoPetsInRange() {
        when(petRepository.findByPriceBetween(100.0, 200.0)).thenReturn(Collections.emptyList());

        List<PetResponseDTO> result = petService.getPetsByPriceRange(100.0, 200.0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


   

    @Test
    void searchPetsByName_ShouldReturnList_WhenMatchFound() {
        try (MockedStatic<PetResponseDTO> mocked = mockStatic(PetResponseDTO.class)) {
            mocked.when(() -> PetResponseDTO.fromEntity(pet)).thenReturn(new PetResponseDTO());
            when(petRepository.findByNameContainingIgnoreCase("bru")).thenReturn(List.of(pet));

            List<PetResponseDTO> result = petService.searchPetsByName("bru");

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void searchPetsByName_ShouldReturnEmptyList_WhenNoMatchFound() {
        when(petRepository.findByNameContainingIgnoreCase("xyz")).thenReturn(Collections.emptyList());

        List<PetResponseDTO> result = petService.searchPetsByName("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }



    @Test
    void searchPetsByBreed_ShouldReturnList_WhenMatchFound() {
        try (MockedStatic<PetResponseDTO> mocked = mockStatic(PetResponseDTO.class)) {
            mocked.when(() -> PetResponseDTO.fromEntity(pet)).thenReturn(new PetResponseDTO());
            when(petRepository.findByBreedContainingIgnoreCase("labra")).thenReturn(List.of(pet));

            List<PetResponseDTO> result = petService.searchPetsByBreed("labra");

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void searchPetsByBreed_ShouldReturnEmptyList_WhenNoMatchFound() {
        when(petRepository.findByBreedContainingIgnoreCase("xyz")).thenReturn(Collections.emptyList());

        List<PetResponseDTO> result = petService.searchPetsByBreed("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }



    @Test
    void updatePet_ShouldReturnSuccess_WhenPetAndCategoryExist() {
        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        requestDTO.setName("Bruno Updated");
        requestDTO.setBreed("Golden Retriever");

        SuccessDTO result = petService.updatePet(1, requestDTO);

        assertNotNull(result);
        assertEquals("Pet updated successfully", result.getMessage());
        assertEquals("Bruno Updated", pet.getName());
        assertEquals("Golden Retriever", pet.getBreed());
        verify(petRepository, times(1)).save(pet);
    }

    @Test
    void updatePet_ShouldThrowException_WhenPetNotFound() {
        when(petRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.updatePet(99, requestDTO)
        );

        assertEquals("Pet not found!", ex.getMessage());
        verify(petRepository, never()).save(any());
    }

    @Test
    void updatePet_ShouldThrowException_WhenCategoryNotFound() {
        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.updatePet(1, requestDTO)
        );

        assertEquals("Pet category not found!", ex.getMessage());
        verify(petRepository, never()).save(any());
    }



    @Test
    void deletePet_ShouldReturnSuccess_WhenPetExists() {
        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        doNothing().when(petRepository).delete(pet);

        SuccessDTO result = petService.deletePet(1);

        assertNotNull(result);
        assertEquals("Pet deleted successfully", result.getMessage());
        verify(petRepository, times(1)).delete(pet);
    }

    @Test
    void deletePet_ShouldThrowException_WhenPetNotFound() {
        when(petRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> petService.deletePet(99)
        );

        assertEquals("Pet not found!", ex.getMessage());
        verify(petRepository, never()).delete(any());
    }
}