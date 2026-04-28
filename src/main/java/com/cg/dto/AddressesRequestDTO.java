package com.cg.dto;
import jakarta.validation.constraints.*;
public class AddressesRequestDTO {
	
	@NotBlank(message = "Street cannot be blank")
    @Size(max = 255, message = "Street cannot exceed 255 characters")
	private String street;
	
	@NotBlank(message = "City cannot be blank")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    private String city;
	
	@NotBlank(message = "State cannot be blank")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
	private String state;
	
	@NotBlank(message = "Zip code cannot be blank")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Zip code must be a valid 6-digit Indian PIN code")
	private String zipCode;
	
	public AddressesRequestDTO() {
	}

	public AddressesRequestDTO(String street, String city, String state, String zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	
}
