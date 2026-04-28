package com.cg.dto;

import java.math.BigDecimal;

import com.cg.entity.PetFood;

public class PetFoodResponseDTO {

    private Integer foodId;
    private String name;
    private String brand;
    private String type;
    private Integer quantity;
    private BigDecimal price;

    public PetFoodResponseDTO() {}

    public PetFoodResponseDTO(Integer foodId, String name, String brand, String type,
                               Integer quantity, BigDecimal price) {
        this.foodId = foodId;
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }
    public static PetFoodResponseDTO fromEntity(PetFood petFood) {
        return new PetFoodResponseDTO(
            petFood.getFoodId(),
            petFood.getName(),
            petFood.getBrand(),
            petFood.getType(),
            petFood.getQuantity(),
            petFood.getPrice()
        );
    }

    public Integer getFoodId() { return foodId; }
    public void setFoodId(Integer foodId) { this.foodId = foodId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}