package com.cg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.AddressesResponseDTO;
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

@Service
public class CustomersServiceImpl implements CustomersService {

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private AddressesRepository addressesRepository;

    private AddressesResponseDTO convertAddressToDTO(Addresses address) {
        if (address == null) {
            return null;
        }
        AddressesResponseDTO addressDTO = new AddressesResponseDTO();
        addressDTO.setAddressId(address.getAddressId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setZipCode(address.getZipCode());
        return addressDTO;
    }

    private CustomersResponseDTO convertToResponseDTO(Customers customer) {
        CustomersResponseDTO responseDTO = new CustomersResponseDTO();
        responseDTO.setCustomerId(customer.getCustomerId());
        responseDTO.setFirstName(customer.getFirstName());
        responseDTO.setLastName(customer.getLastName());
        responseDTO.setEmail(customer.getEmail());
        responseDTO.setPhoneNumber(customer.getPhoneNumber());
        responseDTO.setAddress(convertAddressToDTO(customer.getAddressId()));
        return responseDTO;
    }

    private Customers convertToEntity(CustomersRequestDTO requestDTO, Addresses address) {
        Customers customer = new Customers();
        customer.setFirstName(requestDTO.getFirstName());
        customer.setLastName(requestDTO.getLastName());
        customer.setEmail(requestDTO.getEmail());
        customer.setPhoneNumber(requestDTO.getPhoneNumber());
        customer.setAddressId(address);
        return customer;
    }

    private void updateEntityFromDTO(CustomersRequestDTO requestDTO, Customers customer, Addresses address) {
        customer.setFirstName(requestDTO.getFirstName());
        customer.setLastName(requestDTO.getLastName());
        customer.setEmail(requestDTO.getEmail());
        customer.setPhoneNumber(requestDTO.getPhoneNumber());
        customer.setAddressId(address);
    }

    private Addresses resolveAddress(CustomersRequestDTO requestDTO) {

        if (requestDTO.getAddressId() != null && requestDTO.getAddress() != null) {
            throw new ResourceNotFoundException("Provide either addressId or new address details, not both");
        }

        if (requestDTO.getAddressId() == null && requestDTO.getAddress() == null) {
            throw new ResourceNotFoundException("Either addressId or new address details must be provided");
        }

        if (requestDTO.getAddressId() != null) {
            return addressesRepository.findById(requestDTO.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Address not found with ID: " + requestDTO.getAddressId()));
        }

        Addresses newAddress = new Addresses();
        newAddress.setStreet(requestDTO.getAddress().getStreet());
        newAddress.setCity(requestDTO.getAddress().getCity());
        newAddress.setState(requestDTO.getAddress().getState());
        newAddress.setZipCode(requestDTO.getAddress().getZipCode());
        return addressesRepository.save(newAddress);
    }

    @Override
    public CustomersResponseDTO createCustomer(CustomersRequestDTO requestDTO) {
        Addresses address = resolveAddress(requestDTO);
        Customers customer = convertToEntity(requestDTO, address);
        Customers savedCustomer = customersRepository.save(customer);
        return convertToResponseDTO(savedCustomer);
    }

    @Override
    public CustomersResponseDTO getCustomerById(Integer customerId) {
        Customers customer = customersRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + customerId));
        return convertToResponseDTO(customer);
    }

    @Override
    public List<CustomersResponseDTO> getAllCustomers() {
        return customersRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CustomersResponseDTO getCustomerByEmail(String email) {
        Customers customer = customersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with email: " + email));
        return convertToResponseDTO(customer);
    }
    
    public List<CustomersResponseDTO> getCustomersByCity(String city){
    	return customersRepository.findByAddressCity(city)
    			.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }
    
    @Override
    public CustomersResponseDTO updateCustomer(Integer customerId, CustomersRequestDTO requestDTO) {
        Customers customer = customersRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + customerId));
        Addresses address = resolveAddress(requestDTO);
        updateEntityFromDTO(requestDTO, customer, address);
        Customers updatedCustomer = customersRepository.save(customer);
        return convertToResponseDTO(updatedCustomer);
    }

    @Override
    public SuccessDTO deleteCustomer(Integer customerId) {
        if (!customersRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + customerId);
        }
        customersRepository.deleteById(customerId);
        return new SuccessDTO("Customer with ID " + customerId + " deleted successfully");
    }

    @Override
    public CustomerTransactionSummaryDTO getCustomerTransactionSummary(Integer customerId) {

        Customers customer = customersRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with ID: " + customerId));

        List<Transaction> txList = customer.getTransactions();

        List<Integer> transactionIds = new ArrayList<>();
        if (txList != null) {
            for (Transaction t : txList) {
                transactionIds.add(t.getTransactionId());
            }
        }

        long totalTransactions = txList == null ? 0 : txList.size();

        long successfulPurchases = 0;
        if (txList != null) {
            for (Transaction t : txList) {
                if ("SUCCESS".equalsIgnoreCase(t.getTransactionStatus())) {
                    successfulPurchases++;
                }
            }
        }

        CustomerTransactionSummaryDTO summaryDTO = new CustomerTransactionSummaryDTO();
        summaryDTO.setCustomerId(customer.getCustomerId());
        summaryDTO.setFirstName(customer.getFirstName());
        summaryDTO.setLastName(customer.getLastName());
        summaryDTO.setTransactionIds(transactionIds);
        summaryDTO.setTotalTransactions(totalTransactions);
        summaryDTO.setSuccessfulPurchases(successfulPurchases);

        return summaryDTO;
    }
}