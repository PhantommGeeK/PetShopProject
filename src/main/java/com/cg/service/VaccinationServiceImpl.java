package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.VaccinationRequestDTO;
import com.cg.dto.VaccinationResponseDTO;
import com.cg.entity.Vaccination;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.VaccinationRepository;

@Service
public class VaccinationServiceImpl implements VaccinationService {

    @Autowired
    private VaccinationRepository vaccinationRepository;

    
    @Override
    public List<VaccinationResponseDTO> getAllVaccinations() {
        return vaccinationRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

   
    @Override
    public VaccinationResponseDTO getVaccinationById(Integer id) {
        Vaccination v = vaccinationRepository.findById(id)
                .orElseThrow(() -> new 	ResourceNotFoundException("Vaccination not found with id: " + id));
        return convertToResponseDTO(v);
    }

    
    @Override
    public List<VaccinationResponseDTO> getAvailableVaccinations() {
        return vaccinationRepository.findByAvailable(true)  // true = available
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    
    @Override
    public VaccinationResponseDTO addVaccination(VaccinationRequestDTO dto) {

        
        Vaccination v = new Vaccination();
        v.setName(dto.getName());
        v.setDescription(dto.getDescription());
        v.setPrice(dto.getPrice());
        v.setAvailable(dto.getAvailable());

        
        Vaccination saved = vaccinationRepository.save(v);
        return convertToResponseDTO(saved);
    }

   
    @Override
    public VaccinationResponseDTO updateVaccination(Integer id, VaccinationRequestDTO dto) {

        
        Vaccination v = vaccinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vaccination not found with id: " + id));

        v.setName(dto.getName());
        v.setDescription(dto.getDescription());
        v.setPrice(dto.getPrice());
        v.setAvailable(dto.getAvailable());

        Vaccination updated = vaccinationRepository.save(v);
        return convertToResponseDTO(updated);
    }

    
    @Override
    public void deleteVaccination(Integer id) {
        if (!vaccinationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vaccination not found with id: " + id);
        }
        vaccinationRepository.deleteById(id);
    }

    private VaccinationResponseDTO convertToResponseDTO(Vaccination v) {
        return new VaccinationResponseDTO(
                v.getVaccinationId(),
                v.getName(),
                v.getDescription(),
                v.getPrice(),
                v.getAvailable()
        );
    }
}