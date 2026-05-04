package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.CustomersResponseDTO;
import com.cg.dto.PetCategoryResponseDTO;
import com.cg.dto.PetResponseDTO;
import com.cg.dto.TransactionRequestDTO;
import com.cg.dto.TransactionResponseDTO;
import com.cg.entity.Customers;
import com.cg.entity.GroomingServices;
import com.cg.entity.Pet;
import com.cg.entity.PetFood;
import com.cg.entity.Transaction;
import com.cg.entity.Vaccination;
import com.cg.exception.InvalidRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.CustomersRepository;
import com.cg.repo.GroomingServicesRepository;
import com.cg.repo.PetFoodRepository;
import com.cg.repo.PetRepository;
import com.cg.repo.TransactionRepository;
import com.cg.repo.VaccinationRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetFoodRepository petFoodRepository;

    @Autowired
    private GroomingServicesRepository groomingServicesRepository;

    @Autowired
    private VaccinationRepository vaccinationRepository;

   
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
    @Transactional
    public TransactionResponseDTO addTransaction(TransactionRequestDTO dto) {

        
        Customers customer = customersRepository.findById(dto.getCustomerId())
        		.orElseThrow(() -> new ResourceNotFoundException("Customer", dto.getCustomerId()));

        
        Pet pet = null;
        if (dto.getPetId() != null) {
            pet = petRepository.findById(dto.getPetId())
            		.orElseThrow(() -> new ResourceNotFoundException("Pet",dto.getPetId()));
        }

        
        Transaction t = new Transaction();
        t.setTransactionDate(dto.getTransactionDate());
        t.setAmount(dto.getAmount());
        t.setTransactionStatus(dto.getTransactionStatus());
        t.setItemType(dto.getItemType());
        t.setItemName(dto.getItemName());
        t.setQuantity(dto.getQuantity());
        t.setCustomer(customer);
        t.setPet(pet);

        
        Transaction saved = transactionRepository.save(t);
        reducePetFoodQuantityIfSuccessful(dto);
        markServiceUnavailableIfSuccessful(dto);
        return convertToResponseDTO(saved);
    }

    private void reducePetFoodQuantityIfSuccessful(TransactionRequestDTO dto) {
        if (!"SUCCESS".equalsIgnoreCase(dto.getTransactionStatus())
                || !"FOOD".equalsIgnoreCase(dto.getItemType())
                || dto.getFoodId() == null) {
            return;
        }

        PetFood petFood = petFoodRepository.findById(dto.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("PetFood", dto.getFoodId()));

        int currentQuantity = petFood.getQuantity() == null ? 0 : petFood.getQuantity();
        if (currentQuantity <= 0) {
            throw new InvalidRequestException("Pet food is out of stock");
        }

        petFood.setQuantity(currentQuantity - 1);
        petFoodRepository.save(petFood);
    }

    private void markServiceUnavailableIfSuccessful(TransactionRequestDTO dto) {
        if (!"SUCCESS".equalsIgnoreCase(dto.getTransactionStatus())) {
            return;
        }

        if ("GROOMING".equalsIgnoreCase(dto.getItemType()) && dto.getGroomingServiceId() != null) {
            GroomingServices service = groomingServicesRepository.findById(dto.getGroomingServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Grooming Service", dto.getGroomingServiceId()));
            if (service.isAvailable()) {
                service.setAvailable(false);
                groomingServicesRepository.save(service);
            }
            return;
        }

        if ("VACCINATION".equalsIgnoreCase(dto.getItemType()) && dto.getVaccinationId() != null) {
            Vaccination vaccination = vaccinationRepository.findById(dto.getVaccinationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vaccination", dto.getVaccinationId()));
            if (Boolean.TRUE.equals(vaccination.getAvailable())) {
                vaccination.setAvailable(false);
                vaccinationRepository.save(vaccination);
            }
        }
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

        PetResponseDTO petDTO = null;
        if (t.getPet() != null) {
            petDTO = new PetResponseDTO();
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
        }

        return new TransactionResponseDTO(
                t.getTransactionId(),
                t.getTransactionDate(),
                t.getAmount(),
                t.getTransactionStatus(),
                t.getItemType(),
                t.getItemName(),
                t.getQuantity(),
                customerDTO,
                petDTO
        );
    }
}
