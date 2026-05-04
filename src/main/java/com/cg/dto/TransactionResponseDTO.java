package com.cg.dto;

import java.time.LocalDate;

public class TransactionResponseDTO {

    private Integer transactionId;
    private LocalDate transactionDate;
    private Double amount;
    private String transactionStatus;
    private String itemType;
    private String itemName;
    private Integer quantity;

  
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

	public TransactionResponseDTO(Integer transactionId, LocalDate transactionDate, Double amount,
			String transactionStatus, String itemType, String itemName, Integer quantity,
			CustomersResponseDTO customers, PetResponseDTO pet) {
		this(transactionId, transactionDate, amount, transactionStatus, customers, pet);
		this.itemType = itemType;
		this.itemName = itemName;
		this.quantity = quantity;
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

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
