package com.cg.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Supplier;
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> 
{
	List<Supplier> findByNameContainingIgnoreCase(String name);

	List<Supplier> findByAddress_CityIgnoreCase(String city);

}
