package com.cg.service;

import java.util.List;

import com.cg.dto.PetCategoryRequestDTO;
import com.cg.dto.PetCategoryResponseDTO;

public interface PetCategoryService {

    PetCategoryResponseDTO createCategory(PetCategoryRequestDTO requestDTO);
    PetCategoryResponseDTO getCategoryById(Integer categoryId);
    List<PetCategoryResponseDTO> getAllCategories();
    PetCategoryResponseDTO updateCategory(Integer categoryId, PetCategoryRequestDTO requestDTO);
    void deleteCategory(Integer categoryId);
}