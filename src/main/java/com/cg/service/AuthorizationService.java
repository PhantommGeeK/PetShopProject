package com.cg.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cg.repo.CustomersRepository;
import com.cg.repo.EmployeeRepository;
import com.cg.repo.SupplierRepository;

@Service("authz")
public class AuthorizationService {

    private final CustomersRepository customersRepository;
    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;

    public AuthorizationService(CustomersRepository customersRepository,
                                EmployeeRepository employeeRepository,
                                SupplierRepository supplierRepository) {
        this.customersRepository = customersRepository;
        this.employeeRepository = employeeRepository;
        this.supplierRepository = supplierRepository;
    }

    public boolean isCustomerOwner(Integer customerId, Authentication authentication) {
        return isAuthenticated(authentication)
                && customersRepository.existsByCustomerIdAndUserUsername(customerId, authentication.getName());
    }

    public boolean isCustomerEmailOwner(String email, Authentication authentication) {
        return isAuthenticated(authentication)
                && customersRepository.existsByEmailAndUserUsername(email, authentication.getName());
    }

    public boolean isEmployeeOwner(Integer employeeId, Authentication authentication) {
        return isAuthenticated(authentication)
                && employeeRepository.existsByEmployeeIdAndUserUsername(employeeId, authentication.getName());
    }

    public boolean isSupplierOwner(Integer supplierId, Authentication authentication) {
        return isAuthenticated(authentication)
                && supplierRepository.existsBySupplierIdAndUserUsername(supplierId, authentication.getName());
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}
