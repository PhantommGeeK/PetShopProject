package com.cg.dto;

import jakarta.validation.constraints.*;

public class PetRequestDTO {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets and spaces")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Breed is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Breed must contain only alphabets and spaces")
    @Size(min = 2, max = 50, message = "Breed must be between 2 and 50 characters")
    private String breed;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age cannot be negative")
    private Integer age;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.1", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private Double price;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @Size(max = 500, message = "Description too long")
    private String description;

    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "Image URL must be valid"
    )
    private String imageUrl;
    
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}