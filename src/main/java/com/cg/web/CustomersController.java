package com.cg.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.CustomerTransactionSummaryDTO;
import com.cg.dto.CustomersRequestDTO;
import com.cg.dto.CustomersResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.CustomersService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    @Autowired
    private CustomersService customersService;

    @GetMapping
    public ResponseEntity<List<CustomersResponseDTO>> getAllCustomers() {
        List<CustomersResponseDTO> customers = customersService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomersResponseDTO> getCustomerById(@PathVariable Integer id) {
        CustomersResponseDTO customer = customersService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomersResponseDTO> getCustomerByEmail(@PathVariable String email) {
        CustomersResponseDTO customer = customersService.getCustomerByEmail(email);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<CustomerTransactionSummaryDTO> getCustomerTransactions(
            @PathVariable Integer id) {
        CustomerTransactionSummaryDTO summary = customersService.getCustomerTransactionSummary(id);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/city/{city}")
    public ResponseEntity<List<CustomersResponseDTO>> getCustomersByCity(@PathVariable String city) {
		List<CustomersResponseDTO> customers = customersService.getCustomersByCity(city);
		return ResponseEntity.ok(customers);
	}

//    @PostMapping
//    public ResponseEntity<CustomersResponseDTO> createCustomer(
//            @Valid @RequestBody CustomersRequestDTO requestDTO) {
//        CustomersResponseDTO savedCustomer = customersService.createCustomer(requestDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomersResponseDTO> updateCustomer(
            @PathVariable Integer id,
            @Valid @RequestBody CustomersRequestDTO requestDTO) {
        CustomersResponseDTO updatedCustomer = customersService.updateCustomer(id, requestDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessDTO> deleteCustomer(@PathVariable Integer id) {
        SuccessDTO response = customersService.deleteCustomer(id);
        return ResponseEntity.ok(response);
    }
}