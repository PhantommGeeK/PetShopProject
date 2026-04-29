package com.cg.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>  
{
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
}
