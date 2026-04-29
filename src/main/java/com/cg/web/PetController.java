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

import com.cg.dto.PetRequestDTO;
import com.cg.dto.PetResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.PetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pets")
public class PetController 
{
	@Autowired
	private PetService petService;
	
	@PostMapping
	public ResponseEntity<SuccessDTO> addPet(@Valid @RequestBody PetRequestDTO dto)
	{
		return new ResponseEntity<SuccessDTO>(petService.addPet(dto),HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<PetResponseDTO>> getAllPets()
	{
		return ResponseEntity.ok(petService.getAllPets());
	}

	@GetMapping("/{petId}")
	public ResponseEntity<PetResponseDTO> getPetById(@PathVariable Integer petId)
	{
		return ResponseEntity.ok(petService.getPetById(petId));
	}
	
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<PetResponseDTO>> getPetByCategory(@PathVariable Integer categoryId)
	{
		return ResponseEntity.ok(petService.getPetsByCategory(categoryId));
	}
	
	
	@PutMapping("/{petId}")
	public ResponseEntity<SuccessDTO> updatePet(@PathVariable Integer petId, @RequestBody PetRequestDTO dto)
	{
		return ResponseEntity.ok(petService.updatePet(petId, dto));
	}
	
	@DeleteMapping("/{petId}")
	public ResponseEntity<SuccessDTO> deletePet(@PathVariable Integer petId)
	{
		return ResponseEntity.ok(petService.deletePet(petId));
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<PetResponseDTO>> getPets(
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) Integer age,
	        @RequestParam(required = false) String breed,
	        @RequestParam(required = false) Double minPrice,
	        @RequestParam(required = false) Double maxPrice) {

	    if (name != null) {
	        return ResponseEntity.ok(petService.searchPetsByName(name));
	    }

	    if (age != null) {
	        return ResponseEntity.ok(petService.getPetsByAge(age));
	    }
	    
	    if (breed != null) {
	        return ResponseEntity.ok(petService.searchPetsByBreed(breed));
	    }

	    if (minPrice != null && maxPrice != null) {
	        return ResponseEntity.ok(petService.getPetsByPriceRange(minPrice, maxPrice));
	    }

	    return ResponseEntity.ok(petService.getAllPets());
	}
	
	
}
