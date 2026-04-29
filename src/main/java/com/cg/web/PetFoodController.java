package com.cg.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.PetFoodRequestDTO;
import com.cg.dto.PetFoodResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.PetFoodService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pet-foods")
public class PetFoodController {

    @Autowired
    private PetFoodService petFoodService;

    @PostMapping
    public ResponseEntity<PetFoodResponseDTO> createPetFood(@Valid @RequestBody PetFoodRequestDTO requestDTO) {
        PetFoodResponseDTO response = petFoodService.createPetFood(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PetFoodResponseDTO>> getAllPetFoods() {
        List<PetFoodResponseDTO> response = petFoodService.getAllPetFoods();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{foodId}")
    public ResponseEntity<PetFoodResponseDTO> getPetFoodById(@PathVariable Integer foodId) {
        PetFoodResponseDTO response = petFoodService.getPetFoodById(foodId);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PetFoodResponseDTO>> getPetFoodsByType(@PathVariable String type) {
        List<PetFoodResponseDTO> response = petFoodService.getPetFoodsByType(type);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{foodId}")
    public ResponseEntity<PetFoodResponseDTO> updatePetFood(
            @PathVariable Integer foodId,
            @Valid @RequestBody PetFoodRequestDTO requestDTO) {
        PetFoodResponseDTO response = petFoodService.updatePetFood(foodId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<SuccessDTO> deletePetFood(@PathVariable Integer foodId) {
        SuccessDTO response = petFoodService.deletePetFood(foodId);
        return ResponseEntity.ok(response);
    }
}