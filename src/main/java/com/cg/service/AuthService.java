package com.cg.service;

import com.cg.dto.CustomerRegisterDTO;
import com.cg.dto.EmployeeRegisterDTO;
import com.cg.dto.LoginRequestDTO;
import com.cg.dto.LoginResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRegisterDTO;
import com.cg.entity.RoleType;
import com.cg.entity.User;

public interface AuthService 
{
	User createUser(String username, String password, RoleType roleType);

    SuccessDTO registerCustomer(CustomerRegisterDTO dto);
    SuccessDTO registerSupplier(SupplierRegisterDTO dto);
    SuccessDTO registerEmployee(EmployeeRegisterDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);
    
    User getCurrentUser();
}
