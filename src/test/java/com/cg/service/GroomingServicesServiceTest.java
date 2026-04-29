package com.cg.service;

import com.cg.dto.GroomingServicesRequestDTO;
import com.cg.dto.GroomingServicesResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.GroomingServices;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.GroomingServicesRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroomingServicesServiceTest {

    @Mock
    private GroomingServicesRepository repository;

    @InjectMocks
    private GroomingServicesServiceImpl groomingServicesService;

    private GroomingServices groomingService;
    private GroomingServicesRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        groomingService = new GroomingServices();
        groomingService.setServiceId(1);
        groomingService.setName("Bath & Brush");
        groomingService.setDescription("Full bath and brushing service");
        groomingService.setPrice(500.0);
        groomingService.setAvailable(true);

        requestDTO = new GroomingServicesRequestDTO();
        requestDTO.setName("Bath & Brush");
        requestDTO.setDescription("Full bath and brushing service");
        requestDTO.setPrice(500.0);
        requestDTO.setAvailable(true);
    }


    // =======================================================
    //  CREATE SERVICE
    // =======================================================

    @Test
    void createService_ShouldReturnSuccess_WhenValidRequest() {
        when(repository.save(any(GroomingServices.class))).thenReturn(groomingService);

        SuccessDTO result = groomingServicesService.createService(requestDTO);

        assertNotNull(result);
        assertEquals("Service created successfully", result.getMessage());
        verify(repository, times(1)).save(any(GroomingServices.class));
    }


    // =======================================================
    //  GET SERVICE BY ID
    // =======================================================

    @Test
    void getServiceById_ShouldReturnService_WhenServiceExists() {
        when(repository.findById(1)).thenReturn(Optional.of(groomingService));

        GroomingServicesResponseDTO result = groomingServicesService.getServiceById(1);

        assertNotNull(result);
        assertEquals(1, result.getServiceId());
        assertEquals("Bath & Brush", result.getName());
        assertEquals("Full bath and brushing service", result.getDescription());
        assertEquals(500.0, result.getPrice());
        assertTrue(result.isAvailable());
    }

    @Test
    void getServiceById_ShouldThrowException_WhenServiceNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> groomingServicesService.getServiceById(99)
        );

        assertEquals("Service not found", ex.getMessage());
    }


    // =======================================================
    //  GET ALL SERVICES
    // =======================================================

    @Test
    void getAllServices_ShouldReturnList_WhenServicesExist() {
        GroomingServices service2 = new GroomingServices();
        service2.setServiceId(2);
        service2.setName("Nail Trim");
        service2.setDescription("Nail trimming service");
        service2.setPrice(200.0);
        service2.setAvailable(true);

        when(repository.findAll()).thenReturn(Arrays.asList(groomingService, service2));

        List<GroomingServicesResponseDTO> result = groomingServicesService.getAllServices();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bath & Brush", result.get(0).getName());
        assertEquals("Nail Trim", result.get(1).getName());
    }

    @Test
    void getAllServices_ShouldReturnEmptyList_WhenNoServicesExist() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<GroomingServicesResponseDTO> result = groomingServicesService.getAllServices();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    // =======================================================
    //  GET AVAILABLE SERVICES
    // =======================================================

    @Test
    void getAvailableServices_ShouldReturnOnlyAvailableServices() {
        when(repository.findByAvailableTrue()).thenReturn(List.of(groomingService));

        List<GroomingServicesResponseDTO> result = groomingServicesService.getAvailableServices();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
    }

    @Test
    void getAvailableServices_ShouldReturnEmptyList_WhenNoneAvailable() {
        when(repository.findByAvailableTrue()).thenReturn(Collections.emptyList());

        List<GroomingServicesResponseDTO> result = groomingServicesService.getAvailableServices();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    // =======================================================
    //  UPDATE SERVICE
    // =======================================================

    @Test
    void updateService_ShouldReturnSuccess_WhenServiceExists() {
        when(repository.findById(1)).thenReturn(Optional.of(groomingService));
        when(repository.save(any(GroomingServices.class))).thenReturn(groomingService);

        requestDTO.setName("Deluxe Bath");
        requestDTO.setPrice(800.0);

        SuccessDTO result = groomingServicesService.updateService(1, requestDTO);

        assertNotNull(result);
        assertEquals("Service updated successfully", result.getMessage());
        assertEquals("Deluxe Bath", groomingService.getName());
        assertEquals(800.0, groomingService.getPrice());
        verify(repository, times(1)).save(groomingService);
    }

    @Test
    void updateService_ShouldThrowException_WhenServiceNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> groomingServicesService.updateService(99, requestDTO)
        );

        assertEquals("Service not found", ex.getMessage());
        verify(repository, never()).save(any());
    }


    // =======================================================
    //  TOGGLE AVAILABILITY
    // =======================================================

    @Test
    void toggleAvailability_ShouldSetFalse_WhenCurrentlyTrue() {
        groomingService.setAvailable(true);
        when(repository.findById(1)).thenReturn(Optional.of(groomingService));
        when(repository.save(any(GroomingServices.class))).thenReturn(groomingService);

        GroomingServicesResponseDTO result = groomingServicesService.toggleAvailability(1);

        assertNotNull(result);
        assertFalse(result.isAvailable());
        verify(repository, times(1)).save(groomingService);
    }

    @Test
    void toggleAvailability_ShouldSetTrue_WhenCurrentlyFalse() {
        groomingService.setAvailable(false);
        when(repository.findById(1)).thenReturn(Optional.of(groomingService));
        when(repository.save(any(GroomingServices.class))).thenReturn(groomingService);

        GroomingServicesResponseDTO result = groomingServicesService.toggleAvailability(1);

        assertNotNull(result);
        assertTrue(result.isAvailable());
    }

    @Test
    void toggleAvailability_ShouldThrowException_WhenServiceNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> groomingServicesService.toggleAvailability(99)
        );

        assertEquals("Service not found", ex.getMessage());
    }


    // =======================================================
    //  DELETE SERVICE
    // =======================================================

    @Test
    void deleteService_ShouldReturnSuccess_WhenServiceExists() {
        when(repository.existsById(1)).thenReturn(true);
        doNothing().when(repository).deleteById(1);

        SuccessDTO result = groomingServicesService.deleteService(1);

        assertNotNull(result);
        assertEquals("Services deleted successfully", result.getMessage());
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void deleteService_ShouldThrowException_WhenServiceNotFound() {
        when(repository.existsById(99)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> groomingServicesService.deleteService(99)
        );

        assertEquals("Service not found", ex.getMessage());
        verify(repository, never()).deleteById(anyInt());
    }
}