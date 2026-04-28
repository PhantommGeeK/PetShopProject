package com.cg.service;

import com.cg.dto.TransactionRequestDTO;
import com.cg.dto.TransactionResponseDTO;
import java.util.List;

public interface TransactionService {

   
    List<TransactionResponseDTO> getAllTransactions();

    
    TransactionResponseDTO getTransactionById(Integer id);

    
    List<TransactionResponseDTO> getByStatus(String status);

   
    TransactionResponseDTO addTransaction(TransactionRequestDTO dto);

   
    TransactionResponseDTO updateTransactionStatus(Integer id, String status);
}