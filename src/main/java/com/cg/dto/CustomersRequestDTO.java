package com.cg.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public class CustomersRequestDTO {
	@NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
    private String phoneNumber;

    private Integer addressId;
    
    @Valid
    private AddressesRequestDTO address;
    
    public CustomersRequestDTO() {
    }
    
    public CustomersRequestDTO(String firstName, String lastName, String email,String phoneNumber, Integer addressId,AddressesRequestDTO address) {
    		this.firstName = firstName;
    		this.lastName = lastName;
    		this.email = email;
    		this.phoneNumber = phoneNumber;
    		this.addressId = addressId;
    		this.address = address;
    }

	public AddressesRequestDTO getAddress() {
		return address;
	}

	public void setAddress(AddressesRequestDTO address) {
		this.address = address;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
}
