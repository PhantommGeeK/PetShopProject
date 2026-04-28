package com.cg.dto;

import java.time.LocalDate;

public class TransactionResponseDTO {

    private Integer transactionId;
    private LocalDate transactionDate;
    private Double amount;
    private String transactionStatus;

  
    private CustomersResponseDTO customers;
    private  PetResponseDTO pet;

    public TransactionResponseDTO() {}

	public TransactionResponseDTO(Integer transactionId, LocalDate transactionDate, Double amount,
			String transactionStatus, CustomersResponseDTO customers, PetResponseDTO pet) {
		super();
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.transactionStatus = transactionStatus;
		this.customers = customers;
		this.pet = pet;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public CustomersResponseDTO getCustomers() {
		return customers;
	}

	public void setCustomers(CustomersResponseDTO customers) {
		this.customers = customers;
	}

	public PetResponseDTO getPet() {
		return pet;
	}

	public void setPet(PetResponseDTO pet) {
		this.pet = pet;
	}
    
    
}