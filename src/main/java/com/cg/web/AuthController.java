package com.cg.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.cg.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController 
{

		@Autowired
	    private AuthService authService;

	    @PostMapping("/register/customer")
	    public ResponseEntity<SuccessDTO> registerCustomer(@Valid @RequestBody CustomerRegisterDTO dto) {
	        return new ResponseEntity<>(authService.registerCustomer(dto),HttpStatus.CREATED);
	    }

	    @PostMapping("/register/supplier")
	    public ResponseEntity<SuccessDTO> registerSupplier(@Valid @RequestBody SupplierRegisterDTO dto) {
	        return new ResponseEntity<>(authService.registerSupplier(dto),HttpStatus.CREATED);
	    }

	    @PostMapping("/register/employee")
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
	        User user = authService.getCurrentUser();

	        UserResponseDTO dto = new UserResponseDTO();
	        dto.setUsername(user.getUsername());
	        dto.setRole(user.getRole().getName());

	        return ResponseEntity.ok(dto);
	    }

}
