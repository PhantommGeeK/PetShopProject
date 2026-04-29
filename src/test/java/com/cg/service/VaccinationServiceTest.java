package com.cg.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.dto.VaccinationRequestDTO;
import com.cg.dto.VaccinationResponseDTO;
import com.cg.entity.Vaccination;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.VaccinationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VaccinationServiceTest {

    @Mock
    private VaccinationRepository vaccinationRepository;

    @InjectMocks
    private VaccinationService service = new VaccinationServiceImpl();

    private Vaccination vaccination;
    private VaccinationRequestDTO requestDTO;

    @BeforeEach
    void setup() {

        vaccination = new Vaccination();
        vaccination.setVaccinationId(1);
        vaccination.setName("Rabies Vaccine");
        vaccination.setDescription("Prevents rabies in dogs");
        vaccination.setPrice(500.0);
        vaccination.setAvailable(true);

        requestDTO = new VaccinationRequestDTO();
        requestDTO.setName("Rabies Vaccine");
        requestDTO.setDescription("Prevents rabies in dogs");
        requestDTO.setPrice(500.0);
        requestDTO.setAvailable(true);
    }

    @Test
    void testGetAllVaccinations_success() {

        Vaccination vaccination2 = new Vaccination();
        vaccination2.setVaccinationId(2);
        vaccination2.setName("Parvovirus Vaccine");
        vaccination2.setDescription("Prevents parvovirus");
        vaccination2.setPrice(600.0);
        vaccination2.setAvailable(false);

        when(vaccinationRepository.findAll()).thenReturn(Arrays.asList(vaccination, vaccination2));

        List<VaccinationResponseDTO> result = service.getAllVaccinations();

        assertEquals(2, result.size());
        assertEquals("Rabies Vaccine", result.get(0).getName());
        assertEquals("Parvovirus Vaccine", result.get(1).getName());
        verify(vaccinationRepository, times(1)).findAll();
    }

    @Test
    void testGetAllVaccinations_emptyList() {

        when(vaccinationRepository.findAll()).thenReturn(Arrays.asList());

        List<VaccinationResponseDTO> result = service.getAllVaccinations();

        assertEquals(0, result.size());
        verify(vaccinationRepository, times(1)).findAll();
    }

    @Test
    void testGetVaccinationById_success() {

        when(vaccinationRepository.findById(1)).thenReturn(Optional.of(vaccination));

        VaccinationResponseDTO result = service.getVaccinationById(1);

        assertEquals(1, result.getVaccinationId());
        assertEquals("Rabies Vaccine", result.getName());
        assertEquals("Prevents rabies in dogs", result.getDescription());
        assertEquals(500.0, result.getPrice());
        assertEquals(true, result.getAvailable());
        verify(vaccinationRepository, times(1)).findById(1);
    }

    @Test
    void testGetVaccinationById_notFound() {

        when(vaccinationRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getVaccinationById(99);
        });
    }
    
    @Test
    void testGetAvailableVaccinations_success() {

        when(vaccinationRepository.findByAvailable(true)).thenReturn(Arrays.asList(vaccination));

        List<VaccinationResponseDTO> result = service.getAvailableVaccinations();

        assertEquals(1, result.size());
        assertEquals("Rabies Vaccine", result.get(0).getName());
        assertEquals(true, result.get(0).getAvailable());
        verify(vaccinationRepository, times(1)).findByAvailable(true);
    }

    @Test
    void testGetAvailableVaccinations_emptyList() {

        when(vaccinationRepository.findByAvailable(true)).thenReturn(Arrays.asList());

        List<VaccinationResponseDTO> result = service.getAvailableVaccinations();

        assertEquals(0, result.size());
        verify(vaccinationRepository, times(1)).findByAvailable(true);
    }
    
    @Test
    void testAddVaccination_success() {

        Mockito.when(vaccinationRepository.save(any(Vaccination.class))).thenReturn(vaccination);

        VaccinationResponseDTO result = service.addVaccination(requestDTO);

        assertEquals(1, result.getVaccinationId());
        assertEquals("Rabies Vaccine", result.getName());
        assertEquals("Prevents rabies in dogs", result.getDescription());
        assertEquals(500.0, result.getPrice());
        assertEquals(true, result.getAvailable());
        verify(vaccinationRepository, times(1)).save(any(Vaccination.class));
    }

    @Test
    void testUpdateVaccination_success() {

        VaccinationRequestDTO updateRequest = new VaccinationRequestDTO();
        updateRequest.setName("Rabies Vaccine Updated");
        updateRequest.setDescription("Updated description");
        updateRequest.setPrice(700.0);
        updateRequest.setAvailable(false);

        Vaccination updatedVaccination = new Vaccination();
        updatedVaccination.setVaccinationId(1);
        updatedVaccination.setName("Rabies Vaccine Updated");
        updatedVaccination.setDescription("Updated description");
        updatedVaccination.setPrice(700.0);
        updatedVaccination.setAvailable(false);

        when(vaccinationRepository.findById(1)).thenReturn(Optional.of(vaccination));
        when(vaccinationRepository.save(any(Vaccination.class))).thenReturn(updatedVaccination);

        VaccinationResponseDTO result = service.updateVaccination(1, updateRequest);

        assertEquals("Rabies Vaccine Updated", result.getName());
        assertEquals(700.0, result.getPrice());
        assertEquals(false, result.getAvailable());
        verify(vaccinationRepository, times(1)).findById(1);
        verify(vaccinationRepository, times(1)).save(any(Vaccination.class));
    }

    @Test
    void testUpdateVaccination_notFound() {

        when(vaccinationRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.updateVaccination(99, requestDTO);
        });
    }

    @Test
    void testDeleteVaccination_success() {

        when(vaccinationRepository.existsById(1)).thenReturn(true);

        service.deleteVaccination(1);

        verify(vaccinationRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteVaccination_notFound() {

        when(vaccinationRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteVaccination(99);
        });
    }
}