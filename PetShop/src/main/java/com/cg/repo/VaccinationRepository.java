package com.cg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Vaccination;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination,Integer>  {

}
