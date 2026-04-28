package com.cg.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.GroomingServicesRequestDTO;
import com.cg.dto.GroomingServicesResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.GroomingServices;
import com.cg.service.GroomingServicesService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/grooming")
public class GroomingServicesController {
	
	 @Autowired
	 private GroomingServicesService service;
	 
	 @GetMapping
	    public ResponseEntity<List<GroomingServicesResponseDTO>> getAllServices() {
	        return ResponseEntity.ok(service.getAllServices());
	    }
	 
	 @GetMapping("/{id}")
	    public ResponseEntity<GroomingServicesResponseDTO> getServiceById(@PathVariable Integer id) {
	        return ResponseEntity.ok(service.getServiceById(id));
	    }
	 
	 @GetMapping("/available")
	    public ResponseEntity<List<GroomingServicesResponseDTO>> getAvailableServices() {
	        return ResponseEntity.ok(service.getAvailableServices());
	    }
	 
	
	    @PostMapping
	    public ResponseEntity<SuccessDTO> createService(
	            @Valid @RequestBody GroomingServicesRequestDTO requestDTO) {

	        return new ResponseEntity<>(service.createService(requestDTO), HttpStatus.CREATED);
	    }

	   
	    @PutMapping("/{id}")
	    public ResponseEntity<SuccessDTO> updateService(
	            @PathVariable int id,
	            @Valid @RequestBody GroomingServicesRequestDTO requestDTO) {

	        return ResponseEntity.ok(service.updateService(id, requestDTO));
	    }

	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<SuccessDTO> deleteService(@PathVariable int id) {
	        return ResponseEntity.ok(service.deleteService(id));
	    }

}
