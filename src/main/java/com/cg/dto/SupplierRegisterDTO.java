package com.cg.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SupplierRegisterDTO 
{
	@NotBlank(message = "Username is required")
	private String username;
	
	@NotBlank(message = "Password is required")
    @Size(min = 6)
	private String password;
	
	@NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only alphabets and spaces")
    @Size(max = 100)
    private String name;

    @Pattern(regexp = "^[A-Za-z ]*$", message = "Contact person must contain only alphabets and spaces")
    @Size(max = 50)
    private String contactPerson;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    private String email;
    
    private AddressesRequestDTO address;

    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public AddressesRequestDTO getAddress() {
		return address;
	}

	public void setAddress(AddressesRequestDTO address) {
		this.address = address;
	}

    
}
