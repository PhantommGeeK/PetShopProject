package com.cg.dto;

import java.util.List;

public class CustomerTransactionSummaryDTO {
    private Integer customerId;
    private String firstName;
    private String lastName;
    private List<Integer> transactionIds;
    private List<TransactionResponseDTO> transactions;
    private Long totalTransactions;
    private Long successfulPurchases;
    private Double totalAmount;

    public CustomerTransactionSummaryDTO() {
    }

    public CustomerTransactionSummaryDTO(Integer customerId, String firstName, String lastName,
            List<Integer> transactionIds, Long totalTransactions, Long successfulPurchases) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.transactionIds = transactionIds;
        this.totalTransactions = totalTransactions;
        this.successfulPurchases = successfulPurchases;
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

	public List<Integer> getTransactionIds() {
		return transactionIds;
	}

	public void setTransactionIds(List<Integer> transactionIds) {
		this.transactionIds = transactionIds;
	}

	public List<TransactionResponseDTO> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionResponseDTO> transactions) {
		this.transactions = transactions;
	}

	public Long getTotalTransactions() {
		return totalTransactions;
	}

	public void setTotalTransactions(Long totalTransactions) {
		this.totalTransactions = totalTransactions;
	}

	public Long getSuccessfulPurchases() {
		return successfulPurchases;
	}

	public void setSuccessfulPurchases(Long successfulPurchases) {
		this.successfulPurchases = successfulPurchases;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

   
}
