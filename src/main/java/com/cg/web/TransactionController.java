package com.cg.web;

import com.cg.dto.TransactionRequestDTO;
import com.cg.dto.TransactionResponseDTO;
import com.cg.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

  
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionResponseDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(transactionService.getByStatus(status));
    }

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE') or @authz.isCustomerOwner(#dto.customerId, authentication)")
    public ResponseEntity<TransactionResponseDTO> create(
            @Valid @RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(transactionService.addTransaction(dto));
    }

    
    @PutMapping("/{id}/status")
    public ResponseEntity<TransactionResponseDTO> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        return ResponseEntity.ok(transactionService.updateTransactionStatus(id, status));
    }
}
