package com.cg.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.cg.dto.PetCategoryRequestDTO;
import com.cg.dto.PetCategoryResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.PetCategory;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PetCategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PetCategoryServiceTest {

    @Mock
    private PetCategoryRepository petCategoryRepository;

    @InjectMocks
    private PetCategoryServiceImpl petCategoryService;

    private PetCategory category;

    @BeforeEach
    void setUp() {
        category = new PetCategory();
        category.setCategoryId(1);
        category.setName("Dog");
    }

    
    @Test
    void testCreateCategory_success() {
        PetCategoryRequestDTO request = new PetCategoryRequestDTO();
        request.setName("Dog");

        when(petCategoryRepository.save(any(PetCategory.class)))
                .thenReturn(category);

        PetCategoryResponseDTO response =
                petCategoryService.createCategory(request);

        assertNotNull(response);
        assertEquals("Dog", response.getName());
        verify(petCategoryRepository, times(1))
                .save(any(PetCategory.class));
    }

    
    @Test
    void testGetCategoryById_success() {
        when(petCategoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        PetCategoryResponseDTO response =
                petCategoryService.getCategoryById(1);

        assertNotNull(response);
        assertEquals("Dog", response.getName());
    }

    @Test
    void testGetCategoryById_notFound() {
        when(petCategoryRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            petCategoryService.getCategoryById(1);
        });
    }

   
    @Test
    void testGetAllCategories_success() {
        List<PetCategory> list = Arrays.asList(category);

        when(petCategoryRepository.findAll())
                .thenReturn(list);

        List<PetCategoryResponseDTO> result =
                petCategoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Dog", result.get(0).getName());
    }

    
    @Test
    void testUpdateCategory_success() {
        PetCategoryRequestDTO request = new PetCategoryRequestDTO();
        request.setName("Cat");

        when(petCategoryRepository.findById(1))
                .thenReturn(Optional.of(category));
        when(petCategoryRepository.save(any(PetCategory.class)))
                .thenReturn(category);

        PetCategoryResponseDTO response =
                petCategoryService.updateCategory(1, request);

        assertEquals("Cat", response.getName());
    }

    @Test
    void testUpdateCategory_notFound() {
        when(petCategoryRepository.findById(1))
                .thenReturn(Optional.empty());

        PetCategoryRequestDTO request = new PetCategoryRequestDTO();
        request.setName("Cat");

        assertThrows(ResourceNotFoundException.class, () -> {
            petCategoryService.updateCategory(1, request);
        });
    }

   
    @Test
    void testDeleteCategory_success() {
        when(petCategoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        doNothing().when(petCategoryRepository).delete(category);

        SuccessDTO response =
                petCategoryService.deleteCategory(1);

        assertNotNull(response);
        assertTrue(response.getMessage()
                .contains("deleted successfully"));

        verify(petCategoryRepository, times(1))
                .delete(category);
    }

    @Test
    void testDeleteCategory_notFound() {
        when(petCategoryRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            petCategoryService.deleteCategory(1);
        });
    }
}