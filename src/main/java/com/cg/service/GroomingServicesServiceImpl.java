package com.cg.service;

import com.cg.dto.GroomingServiceRequestDTO;
import com.cg.dto.GroomingServiceResponseDTO;
import com.cg.entity.GroomingServices;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.GroomingServicesRepository;
import com.cg.service.GroomingServicesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroomingServicesServiceImpl implements GroomingServicesService {

    @Autowired
    private GroomingServicesRepository groomingServicesRepository;

    // ---------------------- Helper: Entity -> ResponseDTO ----------------------
    private GroomingServiceResponseDTO mapToResponseDTO(GroomingServices entity) {
        return new GroomingServiceResponseDTO(
            entity.getServiceId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            entity.isAvailable()
        );
    }

    // ---------------------- Helper: RequestDTO -> Entity ----------------------
    private GroomingServices mapToEntity(GroomingServiceRequestDTO dto) {
        GroomingServices service = new GroomingServices();
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setAvailable(dto.getAvailable());
        return service;
    }

    // ---------------------- Create ----------------------
    @Override
    public GroomingServiceResponseDTO createService(GroomingServiceRequestDTO requestDTO) {
        GroomingServices service = mapToEntity(requestDTO);
        GroomingServices saved = groomingServicesRepository.save(service);
        return mapToResponseDTO(saved);
    }

    // ---------------------- Get By ID ----------------------
    @Override
    @Transactional(readOnly = true)
    public GroomingServiceResponseDTO getServiceById(int serviceId) {
        GroomingServices service = groomingServicesRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Grooming service not found with ID: " + serviceId));
        return mapToResponseDTO(service);
    }

    // ---------------------- Get All ----------------------
    @Override
    @Transactional(readOnly = true)
    public List<GroomingServiceResponseDTO> getAllServices() {
        return groomingServicesRepository.findAll()
            .stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    // ---------------------- Get Available Only ----------------------
    @Override
    @Transactional(readOnly = true)
    public List<GroomingServiceResponseDTO> getAvailableServices() {
        return groomingServicesRepository.findByAvailableTrue()
            .stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    // ---------------------- Update ----------------------
    @Override
    public GroomingServiceResponseDTO updateService(int serviceId,
                                                     GroomingServiceRequestDTO requestDTO) {
        GroomingServices existing = groomingServicesRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Grooming service not found with ID: " + serviceId));

        existing.setName(requestDTO.getName());
        existing.setDescription(requestDTO.getDescription());
        existing.setPrice(requestDTO.getPrice());
        existing.setAvailable(requestDTO.getAvailable());

        GroomingServices updated = groomingServicesRepository.save(existing);
        return mapToResponseDTO(updated);
    }

    // ---------------------- Toggle Availability ----------------------
    @Override
    public GroomingServiceResponseDTO toggleAvailability(int serviceId) {
        GroomingServices existing = groomingServicesRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Grooming service not found with ID: " + serviceId));

        existing.setAvailable(!existing.isAvailable());
        GroomingServices updated = groomingServicesRepository.save(existing);
        return mapToResponseDTO(updated);
    }

    // ---------------------- Delete ----------------------
    @Override
    public void deleteService(int serviceId) {
        GroomingServices existing = groomingServicesRepository.findById(serviceId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Grooming service not found with ID: " + serviceId));
        groomingServicesRepository.delete(existing);
    }
}
