package com.cg.dto;
import jakarta.validation.constraints.*;
public class SupplierRequestDTO 
{
	

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

	    private Integer addressId;

	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }

	    public String getContactPerson() { return contactPerson; }
	    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

	    public String getPhoneNumber() { return phoneNumber; }
	    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public Integer getAddressId() { return addressId; }
	    public void setAddressId(Integer addressId) { this.addressId = addressId; }
	

}
