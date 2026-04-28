package com.cg.dto;

public class CustomersResponseDTO {
	private Integer customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private AddressesResponseDTO address;
    
    public CustomersResponseDTO() {
	}

	public CustomersResponseDTO(Integer customerId, String firstName, String lastName, String email, String phoneNumber,
			AddressesResponseDTO address) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public AddressesResponseDTO getAddress() {
		return address;
	}

	public void setAddress(AddressesResponseDTO address) {
		this.address = address;
	}

    
}
