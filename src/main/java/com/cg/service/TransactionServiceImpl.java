package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.CustomersResponseDTO;
import com.cg.dto.PetCategoryResponseDTO;
import com.cg.dto.PetResponseDTO;
import com.cg.dto.TransactionRequestDTO;
import com.cg.dto.TransactionResponseDTO;
import com.cg.entity.Customers;
import com.cg.entity.Pet;
import com.cg.entity.Transaction;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.CustomersRepository;
import com.cg.repo.PetRepository;
import com.cg.repo.TransactionRepository;

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
        		.orElseThrow(() -> new ResourceNotFoundException("Transaction", id));
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
        		.orElseThrow(() -> new ResourceNotFoundException("Customer", dto.getCustomerId()));

        
        Pet pet = petRepository.findById(dto.getPetId())
        		.orElseThrow(() -> new ResourceNotFoundException("Pet",dto.getPetId()));

        
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
        		.orElseThrow(() -> new ResourceNotFoundException("Transaction", id));

        t.setTransactionStatus(status);
        Transaction updated = transactionRepository.save(t);
        return convertToResponseDTO(updated);
    }

    
    private TransactionResponseDTO convertToResponseDTO(Transaction t) {

        CustomersResponseDTO customerDTO = new CustomersResponseDTO();
        customerDTO.setCustomerId(t.getCustomer().getCustomerId());
        customerDTO.setFirstName(t.getCustomer().getFirstName());
        customerDTO.setLastName(t.getCustomer().getLastName());
        customerDTO.setEmail(t.getCustomer().getEmail());
        customerDTO.setPhoneNumber(t.getCustomer().getPhoneNumber());

        PetResponseDTO petDTO = new PetResponseDTO();
        petDTO.setPetId(t.getPet().getPetId());
        petDTO.setName(t.getPet().getName());
        petDTO.setBreed(t.getPet().getBreed());
        petDTO.setAge(t.getPet().getAge());
        petDTO.setPrice(t.getPet().getPrice());
        petDTO.setDescription(t.getPet().getDescription());
        petDTO.setImageUrl(t.getPet().getImageUrl());

        if (t.getPet().getPetCategory() != null) {
            petDTO.setCategory(
                PetCategoryResponseDTO.fromEntity(t.getPet().getPetCategory())
            );
        }

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