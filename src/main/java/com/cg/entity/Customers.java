
package com.cg.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customers {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;
 
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
 
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
 
    @Column(name = "email", unique = true, length = 100)
    private String email;
 
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
 
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Addresses addressId;
 
    @OneToMany(mappedBy = "customer")
    private List<Transaction> transactions;
    
    public Customers() {
		
	}

	public Customers(Integer customerId, String firstName, String lastName, String email, String phoneNumber,
			Addresses addressId, List<Transaction> transactions) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.addressId = addressId;
		this.transactions = transactions;
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

	public Addresses getAddressId() {
		return addressId;
	}
	
	public void setAddressId(Addresses addressId) {
		this.addressId = addressId;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
    
    
}
