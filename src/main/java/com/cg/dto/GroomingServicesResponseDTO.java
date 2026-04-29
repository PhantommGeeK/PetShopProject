package com.cg.dto;

import java.math.BigDecimal;

public class GroomingServicesResponseDTO {

    private int serviceId;
    private String name;
    private String description;
    private Double price;
    private boolean available;

    
    public GroomingServicesResponseDTO() {}

    public GroomingServicesResponseDTO(int serviceId, String name, String description,
                                       Double price, boolean available) {
        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

   
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }


    public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}