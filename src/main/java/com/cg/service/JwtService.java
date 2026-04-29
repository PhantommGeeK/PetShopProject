package com.cg.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cg.config.JwtConfig;
import com.cg.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService 
{
	@Autowired
	private JwtConfig jwtConfig;
	
	public String generateToken(User user) 
	{
		Claims claims= Jwts.claims()
                .subject(user.getUsername())
                .add("userId", user.getUserId())
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

