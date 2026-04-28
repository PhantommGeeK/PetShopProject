package com.cg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Supplier;
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

}
