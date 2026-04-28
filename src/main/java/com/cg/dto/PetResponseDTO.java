package com.cg.dto;

import com.cg.entity.Pet;

public class PetResponseDTO {

    private Integer petId;
    private String name;
    private String breed;
    private Integer age;
    private Double price;

    private PetCategoryResponseDTO category;

    private String description;
    private String imageUrl;

    public PetResponseDTO() {}

    public PetResponseDTO(Integer petId, String name, String breed, Integer age,
                          Double price, PetCategoryResponseDTO category,
                          String description, String imageUrl) {
        this.petId = petId;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.price = price;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static PetResponseDTO fromEntity(Pet pet) {
        return new PetResponseDTO(
            pet.getPetId(),
            pet.getName(),
            pet.getBreed(),
            pet.getAge(),
            pet.getPrice(),
            PetCategoryResponseDTO.fromEntity(pet.getPetCategory()),
            pet.getDescription(),
            pet.getImageUrl()
        );
    }

    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public PetCategoryResponseDTO getCategory() { return category; }
    public void setCategory(PetCategoryResponseDTO category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}