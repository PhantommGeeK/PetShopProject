package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cg.dto.AddressesRequestDTO;
import com.cg.dto.CustomerRegisterDTO;
import com.cg.dto.EmployeeRegisterDTO;
import com.cg.dto.LoginRequestDTO;
import com.cg.dto.LoginResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRegisterDTO;
import com.cg.entity.Addresses;
import com.cg.entity.Customers;
import com.cg.entity.Employee;
import com.cg.entity.Role;
import com.cg.entity.RoleType;
import com.cg.entity.Supplier;
import com.cg.entity.User;
import com.cg.exception.ResourceNotFoundException;
import com.cg.exception.UserAlreadyExistsException;
import com.cg.exception.BadRequestException;
import com.cg.repo.AddressesRepository;
import com.cg.repo.CustomersRepository;
import com.cg.repo.EmployeeRepository;
import com.cg.repo.RoleRepository;
import com.cg.repo.SupplierRepository;
import com.cg.repo.UserRepository;

@Service
public class AuthServiceImpl implements AuthService
{
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
	private AddressesRepository addressRepository;
	@Autowired
	private CustomersRepository customerRepository;
	@Autowired
	private SupplierRepository supplierRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	

	@Override
	public User createUser(String username, String password, RoleType roleType) {
		if (userRepository.existsByUsername(username)) {
	        throw new UserAlreadyExistsException("Username already exists");
	    }

	    Role role = roleRepository.findByName(roleType.name())
	            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

	    User user = new User();
	    user.setUsername(username);
	    user.setPassword(passwordEncoder.encode(password));
	    user.setRole(role);

	    return userRepository.save(user);
	}


	@Override
	public SuccessDTO registerCustomer(CustomerRegisterDTO dto) {
		User user = createUser(dto.getUsername(), dto.getPassword(), RoleType.ROLE_CUSTOMER);

	    AddressesRequestDTO addressDto = dto.getAddress();
	    Addresses address= new Addresses();
	    address.setStreet(addressDto.getStreet());
	    address.setState(addressDto.getState());
	    address.setZipCode(addressDto.getZipCode());
	    address.setCity(addressDto.getCity());
	    addressRepository.save(address);

	    
	    Customers customer = new Customers();
	    customer.setFirstName(dto.getFirstName());
	    customer.setLastName(dto.getLastName());
	    customer.setEmail(dto.getEmail());
	    customer.setPhoneNumber(dto.getPhoneNumber());
	    customer.setAddress(address);
	    customer.setUser(user);

	    customerRepository.save(customer);

	    return new SuccessDTO("Customer registered successfully");
	}


	@Override
	public SuccessDTO registerSupplier(SupplierRegisterDTO dto) {
		User user = createUser(dto.getUsername(), dto.getPassword(), RoleType.ROLE_SUPPLIER);

		AddressesRequestDTO addressDto = dto.getAddress();
	    Addresses address= new Addresses();
	    address.setStreet(addressDto.getStreet());
	    address.setState(addressDto.getState());
	    address.setZipCode(addressDto.getZipCode());
	    address.setCity(addressDto.getCity());
	    addressRepository.save(address);
	    
	    Supplier supplier = new Supplier();
	    supplier.setName(dto.getName());
	    supplier.setContactPerson(dto.getContactPerson());
	    supplier.setPhoneNumber(dto.getPhoneNumber());
	    supplier.setEmail(dto.getEmail());
	    supplier.setAddress(address);
	    supplier.setUser(user);

	    supplierRepository.save(supplier);

	    return new SuccessDTO("Supplier registered successfully");

	}


	@Override
	public SuccessDTO registerEmployee(EmployeeRegisterDTO dto) {
		User user = createUser(dto.getUsername(), dto.getPassword(), RoleType.ROLE_EMPLOYEE);

		AddressesRequestDTO addressDto = dto.getAddress();
	    Addresses address= new Addresses();
	    address.setStreet(addressDto.getStreet());
	    address.setState(addressDto.getState());
	    address.setZipCode(addressDto.getZipCode());
	    address.setCity(addressDto.getCity());
	    addressRepository.save(address);

	    Employee employee = new Employee();
	    employee.setFirstName(dto.getFirstName());
	    employee.setLastName(dto.getLastName());
	    employee.setPosition(dto.getPosition());
	    employee.setHireDate(dto.getHireDate());
	    employee.setPhoneNumber(dto.getPhoneNumber());
	    employee.setEmail(dto.getEmail());
	    employee.setAddress(address);
	    employee.setUser(user);

	    employeeRepository.save(employee);

	    return new SuccessDTO("Employee registered successfully");
	}
	
	@Override
	public LoginResponseDTO login(LoginRequestDTO dto) {

		try {
	        authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                dto.getUsername(),
	                dto.getPassword()
	            )
	        );
	    } 
	    catch (BadCredentialsException e) {
	        throw new BadRequestException("Incorrect password");
	    } 
	    catch (UsernameNotFoundException e) {
	        throw new ResourceNotFoundException("User not found");
	    }

		User user = userRepository.findByUsername(dto.getUsername())
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    String token = jwtService.generateToken(user);

	    return new LoginResponseDTO(token);
	}
	
	@Override
	public User getCurrentUser()
    {
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated())
            throw new BadRequestException("Unauthorized");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found"));
                

    }
}
