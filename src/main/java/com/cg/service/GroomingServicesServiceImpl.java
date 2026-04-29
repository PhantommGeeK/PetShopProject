package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.GroomingServicesRequestDTO;
import com.cg.dto.GroomingServicesResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.GroomingServices;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.GroomingServicesRepository;

@Service
public class GroomingServicesServiceImpl implements GroomingServicesService {

    @Autowired
    private GroomingServicesRepository repository;

   
    private GroomingServices mapToEntity(GroomingServicesRequestDTO dto) {
        GroomingServices service = new GroomingServices();
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setAvailable(dto.getAvailable());
        return service;
    }

    private GroomingServicesResponseDTO mapToDTO(GroomingServices service) {
        return new GroomingServicesResponseDTO(
                service.getServiceId(),
                service.getName(),
                service.getDescription(),
                service.getPrice(),
                service.isAvailable()
        );
    }

  
    @Override
    public SuccessDTO createService(GroomingServicesRequestDTO requestDTO) {
        GroomingServices service = mapToEntity(requestDTO);
        GroomingServices saved = repository.save(service);
        return new SuccessDTO("Service created successfully");
    }

    
    @Override
    public GroomingServicesResponseDTO getServiceById(int serviceId) {
        GroomingServices service = repository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Grooming Service",serviceId));
        return mapToDTO(service);
    }

    @Override
    public List<GroomingServicesResponseDTO> getAllServices() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    
    @Override
    public List<GroomingServicesResponseDTO> getAvailableServices() {
        return repository.findByAvailableTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

  
    @Override
    public SuccessDTO updateService(int serviceId, GroomingServicesRequestDTO requestDTO) {

        GroomingServices service = repository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        service.setName(requestDTO.getName());
        service.setDescription(requestDTO.getDescription());
        service.setPrice(requestDTO.getPrice());
        service.setAvailable(requestDTO.getAvailable());
        

        GroomingServices saved = repository.save(service);
        return new SuccessDTO("Service updated successfully");
    }

   
    @Override
    public GroomingServicesResponseDTO toggleAvailability(int serviceId) {

        GroomingServices service = repository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        service.setAvailable(!service.isAvailable());

        return mapToDTO(repository.save(service));
    }


    @Override
    public SuccessDTO deleteService(int serviceId) {
        if (!repository.existsById(serviceId)) {
            throw new ResourceNotFoundException("Grooming Service", serviceId);
        }
        repository.deleteById(serviceId);
        return new SuccessDTO("Services deleted successfully");
    }
}