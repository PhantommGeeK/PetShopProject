package com.cg.dto;

import com.cg.entity.PetCategory;

public class PetCategoryResponseDTO {

    private Integer categoryId;
    private String name;

    public PetCategoryResponseDTO() {}

    public PetCategoryResponseDTO(Integer categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public static PetCategoryResponseDTO fromEntity(PetCategory category) {
        return new PetCategoryResponseDTO(
            category.getCategoryId(),
            category.getName()
        );
    }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}