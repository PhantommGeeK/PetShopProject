package com.cg.service;

import java.util.List;

import com.cg.dto.CustomerTransactionSummaryDTO;
import com.cg.dto.CustomersRequestDTO;
import com.cg.dto.CustomersResponseDTO;
import com.cg.dto.SuccessDTO;

public interface CustomersService {
    CustomersResponseDTO createCustomer(CustomersRequestDTO requestDTO);
    CustomersResponseDTO getCustomerById(Integer customerId);
    CustomersResponseDTO getCustomerByEmail(String email);
    List<CustomersResponseDTO> getAllCustomers();
    CustomersResponseDTO updateCustomer(Integer customerId, CustomersRequestDTO requestDTO);
    SuccessDTO deleteCustomer(Integer customerId);
    CustomerTransactionSummaryDTO getCustomerTransactionSummary(Integer customerId);
    List<CustomersResponseDTO> getCustomersByCity(String city);
}