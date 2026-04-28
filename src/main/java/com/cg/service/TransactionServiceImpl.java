package com.cg.service;

import com.cg.dto.*;
import com.cg.entity.*;
import com.cg.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private PetRepository petRepository;

   
    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    
    @Override
    public TransactionResponseDTO getTransactionById(Integer id) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return convertToResponseDTO(t);
    }

 
    @Override
    public List<TransactionResponseDTO> getByStatus(String status) {
        return transactionRepository.findByTransactionStatus(status)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

   
    @Override
    public TransactionResponseDTO addTransaction(TransactionRequestDTO dto) {

        
        Customers customer = customersRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + dto.getCustomerId()));

        
        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found: " + dto.getPetId()));

        
        Transaction t = new Transaction();
        t.setTransactionDate(dto.getTransactionDate());
        t.setAmount(dto.getAmount());
        t.setTransactionStatus(dto.getTransactionStatus());
        t.setCustomer(customer);
        t.setPet(pet);

        
        Transaction saved = transactionRepository.save(t);
        return convertToResponseDTO(saved);
    }

    
    @Override
    public TransactionResponseDTO updateTransactionStatus(Integer id, String status) {
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        t.setTransactionStatus(status); // sirf status update
        Transaction updated = transactionRepository.save(t);
        return convertToResponseDTO(updated);
    }

    
    private TransactionResponseDTO convertToResponseDTO(Transaction t) {

        /
        CustomersResponseDTO customerDTO = new CustomersResponseDTO();
        customerDTO.setCustomerId(t.getCustomer().getCustomerId());
        customerDTO.setCustomerName(t.getCustomer().getCustomerName());

        
        PetResponseDTO petDTO = new PetResponseDTO();
        petDTO.setPetId(t.getPet().getPetId());
        petDTO.setPetName(t.getPet().getPetName());

        return new TransactionResponseDTO(
                t.getTransactionId(),
                t.getTransactionDate(),
                t.getAmount(),
                t.getTransactionStatus(),
                customerDTO,
                petDTO
        );
    }
}