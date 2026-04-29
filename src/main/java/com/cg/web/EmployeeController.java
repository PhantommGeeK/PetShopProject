package com.cg.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cg.dto.EmployeeRequestDTO;
import com.cg.dto.EmployeeResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

   
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable int id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

  
//    @PostMapping
//    public ResponseEntity<SuccessDTO> createEmployee(
//            @Valid @RequestBody EmployeeRequestDTO requestDTO) {
//
//        
//        return new ResponseEntity<>(employeeService.createEmployee(requestDTO), HttpStatus.CREATED);
//    }

   
    @PutMapping("/{id}")
    public ResponseEntity<SuccessDTO> updateEmployee(
            @PathVariable int id,
            @Valid @RequestBody EmployeeRequestDTO requestDTO) {

        return ResponseEntity.ok(employeeService.updateEmployee(id, requestDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessDTO> deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new SuccessDTO("Employee deleted successfully"));
    }
}