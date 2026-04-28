package com.cg.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

}
