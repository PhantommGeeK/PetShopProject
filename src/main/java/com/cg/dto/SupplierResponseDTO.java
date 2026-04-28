package com.cg.dto;

import com.cg.entity.Supplier;

public class SupplierResponseDTO 
{
	    private Integer supplierId;
	    private String name;
	    private String contactPerson;
	    private String phoneNumber;
	    private String email;

	    private AddressesResponseDTO address;

	    public SupplierResponseDTO() {}

	    public SupplierResponseDTO(Integer supplierId, String name, String contactPerson,
	                               String phoneNumber, String email,
	                               AddressesResponseDTO address) {
	        this.supplierId = supplierId;
	        this.name = name;
	        this.contactPerson = contactPerson;
	        this.phoneNumber = phoneNumber;
	        this.email = email;
	        this.address = address;
	    }

	    
	    public static SupplierResponseDTO fromEntity(Supplier supplier) {

	        AddressesResponseDTO addressDto = null;

	        if (supplier.getAddress() != null) {
	            addressDto = AddressesResponseDTO.fromEntity(supplier.getAddress());
	        }

	        return new SupplierResponseDTO(
	            supplier.getSupplierId(),
	            supplier.getName(),
	            supplier.getContactPerson(),
	            supplier.getPhoneNumber(),
	            supplier.getEmail(),
	            addressDto
	        );
	    }

	    public Integer getSupplierId() { return supplierId; }
	    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }

	    public String getContactPerson() { return contactPerson; }
	    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

	    public String getPhoneNumber() { return phoneNumber; }
	    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public AddressesResponseDTO getAddress() { return address; }
	    public void setAddress(AddressesResponseDTO address) { this.address = address; }
	

}
