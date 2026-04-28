package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.PetFoodRequestDTO;
import com.cg.dto.PetFoodResponseDTO;
import com.cg.entity.PetFood;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PetFoodRepository;

@Service
@Transactional
public class PetFoodServiceImpl implements PetFoodService {

    @Autowired
    private PetFoodRepository petFoodRepository;


    @Override
    public PetFoodResponseDTO createPetFood(PetFoodRequestDTO requestDTO) {
        PetFood petFood = mapToEntity(requestDTO);
        PetFood saved = petFoodRepository.save(petFood);
        return PetFoodResponseDTO.fromEntity(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public PetFoodResponseDTO getPetFoodById(Integer foodId) {
        PetFood petFood = petFoodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PetFood not found with id: " + foodId));
        return PetFoodResponseDTO.fromEntity(petFood);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetFoodResponseDTO> getAllPetFoods() {
        return petFoodRepository.findAll()
                .stream()
                .map(PetFoodResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetFoodResponseDTO> getPetFoodsByType(String type) {
        return petFoodRepository.findAll()
                .stream()
                .filter(food -> type.equalsIgnoreCase(food.getType()))
                .map(PetFoodResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public PetFoodResponseDTO updatePetFood(Integer foodId, PetFoodRequestDTO requestDTO) {
        PetFood existing = petFoodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PetFood not found with id: " + foodId));

        existing.setName(requestDTO.getName());
        existing.setBrand(requestDTO.getBrand());
        existing.setType(requestDTO.getType());
        existing.setQuantity(requestDTO.getQuantity());
        existing.setPrice(requestDTO.getPrice());

        PetFood updated = petFoodRepository.save(existing);
        return PetFoodResponseDTO.fromEntity(updated);
    }


    @Override
    public void deletePetFood(Integer foodId) {
        PetFood existing = petFoodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PetFood not found with id: " + foodId));
        petFoodRepository.delete(existing);
    }


    private PetFood mapToEntity(PetFoodRequestDTO dto) {
        PetFood petFood = new PetFood();
        petFood.setName(dto.getName());
        petFood.setBrand(dto.getBrand());
        petFood.setType(dto.getType());
        petFood.setQuantity(dto.getQuantity());
        petFood.setPrice(dto.getPrice());
        return petFood;
    }
}