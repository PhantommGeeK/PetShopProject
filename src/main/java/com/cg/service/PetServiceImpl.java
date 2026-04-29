package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.cg.dto.PetRequestDTO;
import com.cg.dto.PetResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.Pet;
import com.cg.entity.PetCategory;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.PetCategoryRepository;
import com.cg.repo.PetRepository;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetCategoryRepository categoryRepository;

    public PetServiceImpl(PetRepository petRepository,
                          PetCategoryRepository categoryRepository) {
        this.petRepository = petRepository;
        this.categoryRepository = categoryRepository;
    }

   
    @Override
    public SuccessDTO addPet(PetRequestDTO dto) {

        PetCategory category = categoryRepository.findById(dto.getCategoryId())
        		.orElseThrow(() -> new ResourceNotFoundException("PetCategory", dto.getCategoryId()));

        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setBreed(dto.getBreed());
        pet.setAge(dto.getAge());
        pet.setPrice(dto.getPrice());
        pet.setDescription(dto.getDescription());
        pet.setImageUrl(dto.getImageUrl());
        pet.setPetCategory(category);

        petRepository.save(pet);

        return new SuccessDTO("Pet created successfully");
    }

   
    @Override
    
    public List<PetResponseDTO> getAllPets() {
    	if(petRepository.findAll().isEmpty())
    		throw new ResourceNotFoundException("No pets are currently available", "getAllPets");
        return petRepository.findAll()
                .stream()
                .map(PetResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    
    @Override
    public PetResponseDTO getPetById(Integer petId) {

        Pet pet = petRepository.findById(petId)
        	    .orElseThrow(() -> new ResourceNotFoundException("Pet", petId));

        return PetResponseDTO.fromEntity(pet);
    }

    @Override
    public List<PetResponseDTO> getPetsByCategory(Integer categoryId) {

        return petRepository.findByPetCategoryCategoryId(categoryId)
                .stream()
                .map(PetResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


	@Override
	public List<PetResponseDTO> getPetsByAge(Integer age) {
		return petRepository.findByAge(age)
				.stream()
				.map(PetResponseDTO::fromEntity)
				.toList();
	}


	@Override
	public List<PetResponseDTO> getPetsByPriceRange(Double minPrice, Double maxPrice) {
	
		return petRepository.findByPriceBetween(minPrice, maxPrice)
				.stream()
				.map(PetResponseDTO::fromEntity)
				.toList();
	}


	@Override
	public List<PetResponseDTO> searchPetsByName(String name) {
		
		return petRepository.findByNameContainingIgnoreCase(name)
				.stream()
				.map(PetResponseDTO::fromEntity)
				.toList();
	}


	@Override
	public List<PetResponseDTO> searchPetsByBreed(String breed) {
		return petRepository.findByBreedContainingIgnoreCase(breed)
				.stream()
				.map(PetResponseDTO::fromEntity)
				.toList();
	}


	@Override
	public SuccessDTO updatePet(Integer petId, PetRequestDTO dto) {
		Pet pet= petRepository.findById(petId).orElseThrow(()->new ResourceNotFoundException("Pet not found!",dto.getName()));
		PetCategory category= categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(()->new ResourceNotFoundException("PetCategory not found!",dto.getCategoryId()));
		pet.setName(dto.getName());
		pet.setAge(dto.getAge());
		pet.setBreed(dto.getBreed());
		pet.setDescription(dto.getDescription());
		pet.setImageUrl(dto.getImageUrl());
		pet.setPetCategory(category);
		petRepository.save(pet);
		
		return new SuccessDTO("Pet updated successfully");
	}


	@Override
	public SuccessDTO deletePet(Integer petId) {
		Pet pet= petRepository.findById(petId)
				.orElseThrow(()->new ResourceNotFoundException("Pet not found!",petId));
		petRepository.delete(pet);
		return new SuccessDTO("Pet deleted successfully");
	}
    
}