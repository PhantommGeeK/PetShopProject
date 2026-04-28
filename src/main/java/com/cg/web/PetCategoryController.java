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

import com.cg.dto.PetCategoryRequestDTO;
import com.cg.dto.PetCategoryResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.PetCategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pet-categories")
public class PetCategoryController {

    @Autowired
    private PetCategoryService petCategoryService;

    @PostMapping
    public ResponseEntity<PetCategoryResponseDTO> createCategory(@Valid @RequestBody PetCategoryRequestDTO requestDTO) {
        PetCategoryResponseDTO response = petCategoryService.createCategory(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PetCategoryResponseDTO>> getAllCategories() {
        List<PetCategoryResponseDTO> response = petCategoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<PetCategoryResponseDTO> getCategoryById(@PathVariable Integer categoryId) {
        PetCategoryResponseDTO response = petCategoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<PetCategoryResponseDTO> updateCategory(
            @PathVariable Integer categoryId,
            @Valid @RequestBody PetCategoryRequestDTO requestDTO) {
        PetCategoryResponseDTO response = petCategoryService.updateCategory(categoryId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<SuccessDTO> deleteCategory(@PathVariable Integer categoryId) {
        SuccessDTO response = petCategoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(response);
    }
}