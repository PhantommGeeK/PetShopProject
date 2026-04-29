package com.cg.config;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtConfig 
{
	private String secret="w7zQJmF2V9K8xL0yYv3bTnP6qR4sUeX2ZaBcDeFgHiJkLmNoPqRsTuVwXyZ12345";
	private final Long tokenExpiration= 604800l;
	
	
	public SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}


	public Long getTokenExpiration() {
		return tokenExpiration;
	}

}
