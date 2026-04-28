package com.cg.service;

import com.cg.dto.VaccinationRequestDTO;
import com.cg.dto.VaccinationResponseDTO;
import java.util.List;

public interface VaccinationService {

    
    List<VaccinationResponseDTO> getAllVaccinations();

   
    VaccinationResponseDTO getVaccinationById(Integer id);


    List<VaccinationResponseDTO> getAvailableVaccinations();

  
    VaccinationResponseDTO addVaccination(VaccinationRequestDTO dto);

    
    VaccinationResponseDTO updateVaccination(Integer id, VaccinationRequestDTO dto);

    
    void deleteVaccination(Integer id);
}