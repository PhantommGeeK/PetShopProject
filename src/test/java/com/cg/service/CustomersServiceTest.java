package com.cg.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.dto.AddressesRequestDTO;
import com.cg.dto.CustomerTransactionSummaryDTO;
import com.cg.dto.CustomersRequestDTO;
import com.cg.dto.CustomersResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.Addresses;
import com.cg.entity.Customers;
import com.cg.entity.Transaction;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressesRepository;
import com.cg.repo.CustomersRepository;

import java.util.ArrayList;
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
public class CustomersServiceTest {

    @Mock
    private CustomersRepository customersRepository;

    @Mock
    private AddressesRepository addressesRepository;

    @InjectMocks
    private CustomersService service = new CustomersServiceImpl();

    private Addresses address;
    private Customers customer;
    private CustomersRequestDTO requestDTOAddId;
    private CustomersRequestDTO requestDTONewAdd;

    @BeforeEach
    void setup() {

        address = new Addresses();
        address.setAddressId(1);
        address.setStreet("MG Road");
        address.setCity("Delhi");
        address.setState("UP");
        address.setZipCode("201001");

        customer = new Customers();
        customer.setCustomerId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("johndoe@gmail.com");
        customer.setPhoneNumber("9876543210");
        customer.setAddressId(address);
        customer.setTransactions(new ArrayList<>());

        requestDTOAddId = new CustomersRequestDTO();
        requestDTOAddId.setFirstName("John");
        requestDTOAddId.setLastName("Doe");
        requestDTOAddId.setEmail("johndoe@gmail.com");
        requestDTOAddId.setPhoneNumber("9876543210");
        requestDTOAddId.setAddressId(1);

        AddressesRequestDTO newAddressDTO = new AddressesRequestDTO();
        newAddressDTO.setStreet("MG Road");
        newAddressDTO.setCity("Delhi");
        newAddressDTO.setState("UP");
        newAddressDTO.setZipCode("201001");

        requestDTONewAdd = new CustomersRequestDTO();
        requestDTONewAdd.setFirstName("John");
        requestDTONewAdd.setLastName("Doe");
        requestDTONewAdd.setEmail("johndoe@gmail.com");
        requestDTONewAdd.setPhoneNumber("9876543210");
        requestDTONewAdd.setAddress(newAddressDTO);
    }

    @Test
    void testCreateCustomer_withExistingAddress_success() {

        Mockito.when(addressesRepository.findById(1)).thenReturn(Optional.of(address));
        Mockito.when(customersRepository.save(Mockito.any(Customers.class))).thenReturn(customer);

        CustomersResponseDTO result = service.createCustomer(requestDTOAddId);

        assertEquals(1, result.getCustomerId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("johndoe@gmail.com", result.getEmail());
        assertEquals("9876543210", result.getPhoneNumber());
        assertEquals("MG Road", result.getAddress().getStreet());
        verify(customersRepository, times(1)).save(any(Customers.class));
    }

    @Test
    void testCreateCustomer_withNewAddress_success() {

        Mockito.when(addressesRepository.save(any(Addresses.class))).thenReturn(address);
        Mockito.when(customersRepository.save(any(Customers.class))).thenReturn(customer);

        CustomersResponseDTO result = service.createCustomer(requestDTONewAdd);

        assertEquals(1, result.getCustomerId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("johndoe@gmail.com", result.getEmail());
        assertEquals("9876543210", result.getPhoneNumber());
        assertEquals("MG Road", result.getAddress().getStreet());
        assertEquals("Delhi", result.getAddress().getCity());
        assertEquals("UP", result.getAddress().getState());
        assertEquals("201001", result.getAddress().getZipCode());
        verify(addressesRepository, times(1)).save(any(Addresses.class));
        verify(customersRepository, times(1)).save(any(Customers.class));
    }

    @Test
    void testCreateCustomer_bothAddressIdAndAddress_throwsException() {

        AddressesRequestDTO newAddressDTO = new AddressesRequestDTO();
        newAddressDTO.setStreet("MG Road");
        newAddressDTO.setCity("Delhi");
        newAddressDTO.setState("UP");
        newAddressDTO.setZipCode("201001");

        CustomersRequestDTO bothDTO = new CustomersRequestDTO();
        bothDTO.setFirstName("John");
        bothDTO.setLastName("Doe");
        bothDTO.setEmail("johndoe@gmail.com");
        bothDTO.setPhoneNumber("9876543210");
        bothDTO.setAddressId(1);
        bothDTO.setAddress(newAddressDTO);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.createCustomer(bothDTO);
        });

        assertEquals("Provide either addressId or new address details, not both", ex.getMessage());
    }

    @Test
    void testCreateCustomer_neitherAddressIdNorAddress_throwsException() {

        CustomersRequestDTO neitherDTO = new CustomersRequestDTO();
        neitherDTO.setFirstName("John");
        neitherDTO.setLastName("Doe");
        neitherDTO.setEmail("johndoe@gmail.com");
        neitherDTO.setPhoneNumber("9876543210");

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            service.createCustomer(neitherDTO);
        });

        assertEquals("Either addressId or new address details must be provided", ex.getMessage());
    }

    @Test
    void testCreateCustomer_addressIdNotFound_throwsException() {

        when(addressesRepository.findById(99)).thenReturn(Optional.empty());

        CustomersRequestDTO dto = new CustomersRequestDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("johndoe@gmail.com");
        dto.setPhoneNumber("9876543210");
        dto.setAddressId(99);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.createCustomer(dto);
        });
    }

    @Test
    void testGetCustomerById_success() {

        when(customersRepository.findById(1)).thenReturn(Optional.of(customer));

        CustomersResponseDTO result = service.getCustomerById(1);

        assertEquals(1, result.getCustomerId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("johndoe@gmail.com", result.getEmail());
        verify(customersRepository, times(1)).findById(1);
    }

    @Test
    void testGetCustomerById_notFound() {

        when(customersRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getCustomerById(99);
        });
    }

    @Test
    void testGetCustomerByEmail_success() {

        when(customersRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.of(customer));

        CustomersResponseDTO result = service.getCustomerByEmail("johndoe@gmail.com");

        assertEquals("johndoe@gmail.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(customersRepository, times(1)).findByEmail("johndoe@gmail.com");
    }

    @Test
    void testGetCustomerByEmail_notFound() {

        when(customersRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getCustomerByEmail("unknown@gmail.com");
        });
    }

    @Test
    void testGetAllCustomers_success() {

        Customers customer2 = new Customers();
        customer2.setCustomerId(2);
        customer2.setFirstName("Priya");
        customer2.setLastName("Singh");
        customer2.setEmail("priya@gmail.com");
        customer2.setPhoneNumber("9876543211");
        customer2.setAddressId(address);
        customer2.setTransactions(new ArrayList<>());

        when(customersRepository.findAll()).thenReturn(Arrays.asList(customer, customer2));

        List<CustomersResponseDTO> result = service.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Priya", result.get(1).getFirstName());
        verify(customersRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCustomers_emptyList() {

        when(customersRepository.findAll()).thenReturn(Arrays.asList());

        List<CustomersResponseDTO> result = service.getAllCustomers();

        assertEquals(0, result.size());
        verify(customersRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCustomer_success() {

        Customers updatedCustomer = new Customers();
        updatedCustomer.setCustomerId(1);
        updatedCustomer.setFirstName("John Updated");
        updatedCustomer.setLastName("Doe");
        updatedCustomer.setEmail("johndoe@gmail.com");
        updatedCustomer.setPhoneNumber("9876543210");
        updatedCustomer.setAddressId(address);
        updatedCustomer.setTransactions(new ArrayList<>());

        when(customersRepository.findById(1)).thenReturn(Optional.of(customer));
        when(addressesRepository.findById(1)).thenReturn(Optional.of(address));
        when(customersRepository.save(any(Customers.class))).thenReturn(updatedCustomer);

        CustomersResponseDTO result = service.updateCustomer(1, requestDTOAddId);

        assertEquals("John Updated", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("johndoe@gmail.com", result.getEmail());
        verify(customersRepository, times(1)).findById(1);
        verify(customersRepository, times(1)).save(any(Customers.class));
    }

    @Test
    void testUpdateCustomer_notFound() {

        when(customersRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.updateCustomer(99, requestDTOAddId);
        });
    }

    @Test
    void testDeleteCustomer_success() {

        when(customersRepository.existsById(1)).thenReturn(true);

        SuccessDTO result = service.deleteCustomer(1);

        assertEquals("Customer with ID 1 deleted successfully", result.getMessage());
        verify(customersRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteCustomer_notFound() {

        when(customersRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteCustomer(99);
        });
    }

    @Test
    void testGetCustomerTransactionSummary_withTransactions_success() {

        Transaction tx1 = new Transaction();
        tx1.setTransactionId(101);
        tx1.setTransactionStatus("SUCCESS");

        Transaction tx2 = new Transaction();
        tx2.setTransactionId(102);
        tx2.setTransactionStatus("FAILED");

        Transaction tx3 = new Transaction();
        tx3.setTransactionId(103);
        tx3.setTransactionStatus("SUCCESS");

        customer.setTransactions(Arrays.asList(tx1, tx2, tx3));

        when(customersRepository.findById(1)).thenReturn(Optional.of(customer));

        CustomerTransactionSummaryDTO result = service.getCustomerTransactionSummary(1);

        assertEquals(1, result.getCustomerId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(3, result.getTotalTransactions());
        assertEquals(2, result.getSuccessfulPurchases());
        assertEquals(3, result.getTransactionIds().size());
        verify(customersRepository, times(1)).findById(1);
    }

    @Test
    void testGetCustomerTransactionSummary_noTransactions_success() {

        customer.setTransactions(new ArrayList<>());

        when(customersRepository.findById(1)).thenReturn(Optional.of(customer));

        CustomerTransactionSummaryDTO result = service.getCustomerTransactionSummary(1);

        assertEquals(0, result.getTotalTransactions());
        assertEquals(0, result.getSuccessfulPurchases());
        assertEquals(0, result.getTransactionIds().size());
        verify(customersRepository, times(1)).findById(1);
    }

    @Test
    void testGetCustomerTransactionSummary_notFound() {

        when(customersRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getCustomerTransactionSummary(99);
        });
    }
}