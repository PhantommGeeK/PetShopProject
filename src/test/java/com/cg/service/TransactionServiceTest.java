package com.cg.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.dto.TransactionRequestDTO;
import com.cg.dto.TransactionResponseDTO;
import com.cg.entity.Customers;
import com.cg.entity.Pet;
import com.cg.entity.Transaction;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.CustomersRepository;
import com.cg.repo.PetRepository;
import com.cg.repo.TransactionRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomersRepository customersRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private TransactionService service = new TransactionServiceImpl();

    private Customers customer;
    private Pet pet;
    private Transaction transaction;
    private TransactionRequestDTO requestDTO;

    @BeforeEach
    void setup() {

        customer = new Customers();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("johndoe@gmail.com");
        customer.setPhoneNumber("9876543210");

        pet = new Pet();
        pet.setPetId(1);
        pet.setName("Bruno");
        pet.setBreed("Labrador");
        pet.setAge(2);
        pet.setPrice(15000.0);

        transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTransactionDate(LocalDate.of(2024, 1, 15));
        transaction.setAmount(15000.0);
        transaction.setTransactionStatus("SUCCESS");
        transaction.setCustomer(customer);
        transaction.setPet(pet);

        requestDTO = new TransactionRequestDTO();
        requestDTO.setTransactionDate(LocalDate.of(2024, 1, 15));
        requestDTO.setAmount(15000.0);
        requestDTO.setTransactionStatus("SUCCESS");
        requestDTO.setCustomerId(1);
        requestDTO.setPetId(1);
    }

    @Test
    void testGetAllTransactions_success() {

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2);
        transaction2.setTransactionDate(LocalDate.of(2024, 2, 20));
        transaction2.setAmount(20000.0);
        transaction2.setTransactionStatus("FAILED");
        transaction2.setCustomer(customer);
        transaction2.setPet(pet);

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction, transaction2));

        List<TransactionResponseDTO> result = service.getAllTransactions();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getTransactionId());
        assertEquals("SUCCESS", result.get(0).getTransactionStatus());
        assertEquals("FAILED", result.get(1).getTransactionStatus());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTransactions_emptyList() {

        when(transactionRepository.findAll()).thenReturn(Arrays.asList());

        List<TransactionResponseDTO> result = service.getAllTransactions();

        assertEquals(0, result.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetTransactionById_success() {

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        TransactionResponseDTO result = service.getTransactionById(1);

        assertEquals(1, result.getTransactionId());
        assertEquals(15000.0, result.getAmount());
        assertEquals("SUCCESS", result.getTransactionStatus());
        assertEquals("John", result.getCustomers().getFirstName());
        assertEquals("Bruno", result.getPet().getName());
        verify(transactionRepository, times(1)).findById(1);
    }

    @Test
    void testGetTransactionById_notFound() {

        when(transactionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getTransactionById(99);
        });
    }

    @Test
    void testGetByStatus_success() {

        when(transactionRepository.findByTransactionStatus("SUCCESS"))
                .thenReturn(Arrays.asList(transaction));

        List<TransactionResponseDTO> result = service.getByStatus("SUCCESS");

        assertEquals(1, result.size());
        assertEquals("SUCCESS", result.get(0).getTransactionStatus());
        verify(transactionRepository, times(1)).findByTransactionStatus("SUCCESS");
    }

    @Test
    void testGetByStatus_emptyList() {

        when(transactionRepository.findByTransactionStatus("PENDING"))
                .thenReturn(Arrays.asList());

        List<TransactionResponseDTO> result = service.getByStatus("PENDING");

        assertEquals(0, result.size());
        verify(transactionRepository, times(1)).findByTransactionStatus("PENDING");
    }

    @Test
    void testAddTransaction_success() {

        Mockito.when(customersRepository.findById(1)).thenReturn(Optional.of(customer));
        Mockito.when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        Mockito.when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponseDTO result = service.addTransaction(requestDTO);

        assertEquals(1, result.getTransactionId());
        assertEquals(15000.0, result.getAmount());
        assertEquals("SUCCESS", result.getTransactionStatus());
        assertEquals("John", result.getCustomers().getFirstName());
        assertEquals("Bruno", result.getPet().getName());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testAddTransaction_customerNotFound() {

        when(customersRepository.findById(99)).thenReturn(Optional.empty());

        requestDTO.setCustomerId(99);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.addTransaction(requestDTO);
        });
    }

    @Test
    void testAddTransaction_petNotFound() {

        when(customersRepository.findById(1)).thenReturn(Optional.of(customer));
        when(petRepository.findById(99)).thenReturn(Optional.empty());

        requestDTO.setPetId(99);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.addTransaction(requestDTO);
        });
    }

    @Test
    void testUpdateTransactionStatus_success() {

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionId(1);
        updatedTransaction.setTransactionDate(LocalDate.of(2024, 1, 15));
        updatedTransaction.setAmount(15000.0);
        updatedTransaction.setTransactionStatus("FAILED");
        updatedTransaction.setCustomer(customer);
        updatedTransaction.setPet(pet);

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        TransactionResponseDTO result = service.updateTransactionStatus(1, "FAILED");

        assertEquals("FAILED", result.getTransactionStatus());
        verify(transactionRepository, times(1)).findById(1);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testUpdateTransactionStatus_notFound() {

        when(transactionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.updateTransactionStatus(99, "FAILED");
        });
    }
}