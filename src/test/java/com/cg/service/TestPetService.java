package com.cg.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cg.dto.PetRequestDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.Pet;
import com.cg.entity.PetCategory;
import com.cg.entity.Product;
import com.cg.repo.PetCategoryRepository;
import com.cg.repo.PetRepository;

@SpringBootTest
public class TestPetService 
{
	@MockitoBean
	private PetRepository petRepository;
	
	@MockitoBean
	private PetCategoryRepository categoryRepository;
	
	@Autowired
	private PetService petService;
	
	private Pet pet;
    private PetCategory category;
    
	@BeforeEach
	public void beforeEach()
	{
		category = new PetCategory();
        category.setCategoryId(1);
        category.setName("Cat");
		pet= new Pet();
		pet.setPetId(1);
        pet.setName("Tom");
        pet.setAge(2);
        pet.setBreed("Persian");
        pet.setPrice(5000.0);
        pet.setPetCategory(category);
	}
	
	@Test
	void testAddPet() {
	    PetRequestDTO dto = new PetRequestDTO();
	    dto.setName("Tom");
	    dto.setCategoryId(1);
	    dto.setAge(2);
	    dto.setBreed("Persian");
	    dto.setPrice(5000.0);
	    
	    //Mockito.when(categoryRepository.findById(1)).thenReturn(Optional.ofNullable(category));
	    Mockito.when(petRepository.save(Mockito.any(Pet.class))).thenReturn(pet);
	    SuccessDTO result= petService.addPet(dto);
	    assertEquals("Pet created successfully", result.getMessage());
	    Mockito.verify(petRepository).save(Mockito.any(Pet.class));
	}
}
