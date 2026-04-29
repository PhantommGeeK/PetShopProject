package com.cg.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Customers;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, Integer> {
	Optional<Customers> findByEmail(String email);
	List<Customers> findByAddressCity(String city);
}
