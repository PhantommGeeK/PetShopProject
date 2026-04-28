package com.cg.web;

import com.cg.dto.VaccinationRequestDTO;
import com.cg.dto.VaccinationResponseDTO;
import com.cg.service.VaccinationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vaccinations")
public class VaccinationController {

    @Autowired
    private VaccinationService vaccinationService;

    
    @GetMapping
    public ResponseEntity<List<VaccinationResponseDTO>> getAll() {
        return ResponseEntity.ok(vaccinationService.getAllVaccinations());
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<VaccinationResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(vaccinationService.getVaccinationById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<VaccinationResponseDTO>> getAvailable() {
        return ResponseEntity.ok(vaccinationService.getAvailableVaccinations());
    }

  
    @PostMapping
    public ResponseEntity<VaccinationResponseDTO> add(
            @Valid @RequestBody VaccinationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(vaccinationService.addVaccination(dto));
    }

 
    @PutMapping("/{id}")
    public ResponseEntity<VaccinationResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody VaccinationRequestDTO dto) {
        return ResponseEntity.ok(vaccinationService.updateVaccination(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        vaccinationService.deleteVaccination(id);
        return ResponseEntity.ok("Vaccination deleted successfully!");
    }
}