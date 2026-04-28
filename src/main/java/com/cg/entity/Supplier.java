package com.cg.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "suppliers")
public class Supplier 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private Integer supplierId;
	
	private String name;
	@Column(name = "contact_person")
	private String contactPerson;
	
	@Column(name = "phone_number")
	private String phoneNumber;

	private String email;
	
	@ManyToOne
    @JoinColumn(name = "address_id")
    private Addresses address;
	
	@ManyToMany
	@JoinTable(
	    name = "pet_supplier_relationship",
	    joinColumns = @JoinColumn(name = "supplier_id"),
	    inverseJoinColumns = @JoinColumn(name = "pet_id")
	)
	private List<Pet> pets;
	
	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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

	public Addresses getAddress() {
		return address;
	}

	public void setAddress(Addresses address) {
		this.address = address;
	}

	
}
