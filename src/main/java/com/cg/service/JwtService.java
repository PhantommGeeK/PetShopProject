package com.cg.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cg.config.JwtConfig;
import com.cg.entity.RoleType;
import com.cg.entity.User;
import com.cg.repo.CustomersRepository;
import com.cg.repo.EmployeeRepository;
import com.cg.repo.SupplierRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService 
{
	@Autowired
	private JwtConfig jwtConfig;
	@Autowired
	private CustomersRepository customersRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private SupplierRepository supplierRepository;
	
	public String generateToken(User user) 
	{
		Claims claims= Jwts.claims()
                .subject(user.getUsername())
                .add("userId", user.getUserId())
                .add("profileId", getProfileId(user))
                .add("role",user.getRole().getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtConfig.getTokenExpiration()))
                .build();
		
		return Jwts.builder()
                .claims(claims)
                .signWith(jwtConfig.getSecretKey())
                .compact();
       
    }
	
	public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    private Integer getProfileId(User user) {
        String role = user.getRole().getName();

        if (RoleType.ROLE_CUSTOMER.name().equals(role)) {
            return customersRepository.findByUserUsername(user.getUsername())
                    .map(customer -> customer.getCustomerId())
                    .orElse(null);
        }

        if (RoleType.ROLE_EMPLOYEE.name().equals(role)) {
            return employeeRepository.findByUserUsername(user.getUsername())
                    .map(employee -> employee.getEmployeeId())
                    .orElse(null);
        }

        if (RoleType.ROLE_SUPPLIER.name().equals(role)) {
            return supplierRepository.findByUserUsername(user.getUsername())
                    .map(supplier -> supplier.getSupplierId())
                    .orElse(null);
        }

        return null;
    }

    private Claims getClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isTokenExpired(String token)
    {
    	return getClaims(token).getExpiration().before(new Date());
    }
    
    public Boolean validateToken(String token,UserDetails userDetails)
    {
    	Claims claims= getClaims(token);
    	return (claims.getSubject().equals(userDetails.getUsername()) && !isTokenExpired(token) && userDetails.isEnabled());
    }
	
}

