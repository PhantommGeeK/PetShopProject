package com.cg.dto;

public class VaccinationResponseDTO {

    private Integer vaccinationId;
    private String name;
    private String description;
    private Double price;
    private Boolean available;

    public VaccinationResponseDTO() {}

    public VaccinationResponseDTO(Integer vaccinationId, String name, String description,
                                  Double price, Boolean available) {
        this.vaccinationId = vaccinationId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

   

    public Integer getVaccinationId() {
        return vaccinationId;
    }

    public void setVaccinationId(Integer vaccinationId) {
        this.vaccinationId = vaccinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}