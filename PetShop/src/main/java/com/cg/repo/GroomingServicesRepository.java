package com.cg.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.GroomingServices;
@Repository
public interface GroomingServicesRepository extends JpaRepository<GroomingServices, Integer> {

	Optional<GroomingServices> findByAvailableTrue();

}
