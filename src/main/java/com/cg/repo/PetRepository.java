package com.cg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cg.dto.PetResponseDTO;
import com.cg.entity.Pet;
import com.cg.entity.PetCategory;


@Repository
public interface PetRepository extends JpaRepository<Pet, Integer>
{
	public List<Pet> findByPetCategoryCategoryId(Integer categoryId);
	public List<Pet> findByAge(Integer age);
	public List<Pet> findByBreedContainingIgnoreCase(String breed);
	public List<Pet> findByPriceBetween(Double minPrice, Double maxPrice);
	public List<Pet> findByNameContainingIgnoreCase(String name);
	
}
