package com.cg.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.CustomerRegisterDTO;
import com.cg.dto.EmployeeRegisterDTO;
import com.cg.dto.LoginRequestDTO;
import com.cg.dto.LoginResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRegisterDTO;
import com.cg.dto.UserResponseDTO;
import com.cg.entity.User;
import com.cg.repo.CustomersRepository;
import com.cg.repo.EmployeeRepository;
import com.cg.repo.SupplierRepository;
import com.cg.repo.UserRepository;
import com.cg.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController 
{

		@Autowired
	    private AuthService authService;
		@Autowired
		private UserRepository userRepository;
		@Autowired
		private CustomersRepository customersRepository;
		@Autowired
		private EmployeeRepository employeeRepository;
		@Autowired
		private SupplierRepository supplierRepository;

	    @PostMapping("/register/customer")
	    public ResponseEntity<SuccessDTO> registerCustomer(@Valid @RequestBody CustomerRegisterDTO dto) {
	        return new ResponseEntity<>(authService.registerCustomer(dto),HttpStatus.CREATED);
	    }

	    @PostMapping("/register/supplier")
	    public ResponseEntity<SuccessDTO> registerSupplier(@Valid @RequestBody SupplierRegisterDTO dto) {
	        return new ResponseEntity<>(authService.registerSupplier(dto),HttpStatus.CREATED);
	    }

	    @PostMapping("/register/employee")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<SuccessDTO> registerEmployee(@Valid @RequestBody EmployeeRegisterDTO dto) {
	    	
	    	return new ResponseEntity<>(authService.registerEmployee(dto),HttpStatus.CREATED);
	    
	    }
	    
	    @PostMapping("/login")
	    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto)
	    {
	    	return ResponseEntity.ok(authService.login(dto));
	    }
	    
	    @GetMapping("/me")
	    public ResponseEntity<UserResponseDTO> getCurrentUser() {
	    	var authentication = SecurityContextHolder.getContext().getAuthentication();
	    	UserResponseDTO dto = new UserResponseDTO();

	    	if (authentication == null
	    			|| authentication instanceof AnonymousAuthenticationToken
	    			|| !authentication.isAuthenticated()) {
	    		return ResponseEntity.ok(dto);
	    	}

	    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        User user = userRepository.findByUsername(userDetails.getUsername())
	        		.orElseThrow(() -> new com.cg.exception.ResourceNotFoundException("User not found"));

	        dto.setUserId(user.getUserId());
	        dto.setProfileId(resolveProfileId(user));
	        dto.setUsername(user.getUsername());
	        dto.setRole(user.getRole().getName());

	        return ResponseEntity.ok(dto);
	    }

	    private Integer resolveProfileId(User user) {
	    	String role = user.getRole().getName();

	    	if ("ROLE_CUSTOMER".equals(role)) {
	    		return customersRepository.findByUserUsername(user.getUsername())
	    				.map(customer -> customer.getCustomerId())
	    				.orElse(null);
	    	}

	    	if ("ROLE_EMPLOYEE".equals(role)) {
	    		return employeeRepository.findByUserUsername(user.getUsername())
	    				.map(employee -> employee.getEmployeeId())
	    				.orElse(null);
	    	}

	    	if ("ROLE_SUPPLIER".equals(role)) {
	    		return supplierRepository.findByUserUsername(user.getUsername())
	    				.map(supplier -> supplier.getSupplierId())
	    				.orElse(null);
	    	}

	    	return null;
	    }

}
