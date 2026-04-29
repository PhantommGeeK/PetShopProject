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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.PetResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRequestDTO;
import com.cg.dto.SupplierResponseDTO;
import com.cg.service.SupplierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable Integer supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierById(supplierId));
    }

    
    @GetMapping("/{supplierId}/pets")
    public ResponseEntity<List<PetResponseDTO>> getPetsBySupplier(@PathVariable Integer supplierId) {
        return ResponseEntity.ok(supplierService.getPetsBySupplier(supplierId));
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SuccessDTO> updateSupplier(@PathVariable Integer supplierId,
                                                     @RequestBody SupplierRequestDTO dto) {
        return ResponseEntity.ok(supplierService.updateSupplier(supplierId, dto));
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<SuccessDTO> deleteSupplier(@PathVariable Integer supplierId) {
        return ResponseEntity.ok(supplierService.deleteSupplier(supplierId));
    }

    @PostMapping("/{supplierId}/pets/{petId}")
    public ResponseEntity<SuccessDTO> assignPetToSupplier(
            @PathVariable Integer supplierId,
            @PathVariable Integer petId) {

        return ResponseEntity.ok(supplierService.assignPetToSupplier(supplierId, petId));
    }

    @DeleteMapping("/{supplierId}/pets/{petId}")
    public ResponseEntity<SuccessDTO> removePetFromSupplier(
            @PathVariable Integer supplierId,
            @PathVariable Integer petId) {

        return ResponseEntity.ok(supplierService.removePetFromSupplier(supplierId, petId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SupplierResponseDTO>> searchSupplierByName(
            @RequestParam String name) {

        return ResponseEntity.ok(supplierService.searchSupplierByName(name));
    }

   
    @GetMapping("/city")
    public ResponseEntity<List<SupplierResponseDTO>> getSuppliersByCity(
            @RequestParam String city) {

        return ResponseEntity.ok(supplierService.getSuppliersByCity(city));
    }
}
