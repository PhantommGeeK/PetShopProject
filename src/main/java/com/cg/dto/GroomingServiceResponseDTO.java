package com.cg.dto;

import java.math.BigDecimal;

public class GroomingServiceResponseDTO {

    private int serviceId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;

   
    public GroomingServiceResponseDTO() {}

    public GroomingServiceResponseDTO(int serviceId, String name, String description,
                                       BigDecimal price, boolean available) {
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

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}