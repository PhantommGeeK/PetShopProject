package com.cg.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PetFoodRequestDTO {

    @NotBlank(message = "Food name is required")
    @Size(max = 255, message = "Food name must not exceed 255 characters")
    private String name;

    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;

    @Pattern(regexp = "Dry|Wet", message = "Type must be either 'Dry' or 'Wet'")
    private String type;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be zero or more")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    public PetFoodRequestDTO() {}

    public PetFoodRequestDTO(String name, String brand, String type, Integer quantity, BigDecimal price) {
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

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