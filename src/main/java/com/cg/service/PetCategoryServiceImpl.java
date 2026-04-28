package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.PetCategoryRequestDTO;
import com.cg.dto.PetCategoryResponseDTO;
import com.cg.entity.PetCategory;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PetCategoryRepository;

@Service
@Transactional
public class PetCategoryServiceImpl implements PetCategoryService {

    @Autowired
    private PetCategoryRepository petCategoryRepository;
    @Override
    public PetCategoryResponseDTO createCategory(PetCategoryRequestDTO requestDTO) {
        PetCategory category = mapToEntity(requestDTO);
        PetCategory saved = petCategoryRepository.save(category);
        return PetCategoryResponseDTO.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PetCategoryResponseDTO getCategoryById(Integer categoryId) {
        PetCategory category = petCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PetCategory not found with id: " + categoryId));
        return PetCategoryResponseDTO.fromEntity(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetCategoryResponseDTO> getAllCategories() {
        return petCategoryRepository.findAll()
                .stream()
                .map(PetCategoryResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PetCategoryResponseDTO updateCategory(Integer categoryId, PetCategoryRequestDTO requestDTO) {
        PetCategory existing = petCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PetCategory not found with id: " + categoryId));

        existing.setName(requestDTO.getName());

        PetCategory updated = petCategoryRepository.save(existing);
        return PetCategoryResponseDTO.fromEntity(updated);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        PetCategory existing = petCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PetCategory not found with id: " + categoryId));
        petCategoryRepository.delete(existing);
    }

    private PetCategory mapToEntity(PetCategoryRequestDTO dto) {
        PetCategory category = new PetCategory();
        category.setName(dto.getName());
        return category;
    }
}