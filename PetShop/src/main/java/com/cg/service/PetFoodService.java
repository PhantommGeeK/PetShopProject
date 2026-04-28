package com.cg.service;

import java.util.List;

import com.cg.dto.PetFoodRequestDTO;
import com.cg.dto.PetFoodResponseDTO;

public interface PetFoodService {

    PetFoodResponseDTO createPetFood(PetFoodRequestDTO requestDTO);
    PetFoodResponseDTO getPetFoodById(Integer foodId);
    List<PetFoodResponseDTO> getAllPetFoods();
    List<PetFoodResponseDTO> getPetFoodsByType(String type);
    PetFoodResponseDTO updatePetFood(Integer foodId, PetFoodRequestDTO requestDTO);
    void deletePetFood(Integer foodId);
}