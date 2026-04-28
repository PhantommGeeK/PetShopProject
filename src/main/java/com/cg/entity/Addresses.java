package com.cg.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="addresses")
public class Addresses {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer addressId;
	
	@Column(name="street")
	private String street;
	
	@Column(name="city", length = 100)
	private String city;
	
	@Column(name="state", length = 50)
	private String state;
	
	@Column(name="zip_code", length = 20)
	private String zipCode;
	
	@OneToMany(mappedBy = "address")
	private List<Customers> customers;
	
	@OneToMany(mappedBy = "address")
    private List<Supplier> suppliers;
 
	@OneToMany(mappedBy = "address")
    private List<Employee> employees;
	
	public Addresses() {
			
	}
	
	public Addresses(Integer addressId, String street, String city, String state, String zipCode,
			List<Customers> customers, List<Supplier> suppliers, List<Employee> employees) {
		super();
		this.addressId = addressId;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.customers = customers;
		this.suppliers = suppliers;
		this.employees = employees;
	}
	
	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
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

	public List<Customers> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customers> customers) {
		this.customers = customers;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
		

}
