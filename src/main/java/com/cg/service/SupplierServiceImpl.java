package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cg.dto.*;
import com.cg.entity.*;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.*;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final PetRepository petRepository;
    private final AddressesRepository addressRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               PetRepository petRepository,
                               AddressesRepository addressRepository) {
        this.supplierRepository = supplierRepository;
        this.petRepository = petRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public SuccessDTO addSupplier(SupplierRequestDTO dto) {

        Supplier supplier = new Supplier();

        supplier.setName(dto.getName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setEmail(dto.getEmail());

        if (dto.getAddressId() != null) {
            Addresses address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
            supplier.setAddress(address);
        }

        supplierRepository.save(supplier);

        return new SuccessDTO("Supplier added successfully");
    }

    
    @Override
    public SupplierResponseDTO getSupplierById(Integer supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        return SupplierResponseDTO.fromEntity(supplier);
    }
    
    @Override
    public List<SupplierResponseDTO> getAllSuppliers() {

        return supplierRepository.findAll()
                .stream()
                .map(SupplierResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public SuccessDTO updateSupplier(Integer supplierId, SupplierRequestDTO dto) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        supplier.setName(dto.getName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setEmail(dto.getEmail());

        if (dto.getAddressId() != null) {
            Addresses address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
            supplier.setAddress(address);
        }

        supplierRepository.save(supplier);

        return new SuccessDTO("Supplier updated successfully");
    }

    
    @Override
    public SuccessDTO deleteSupplier(Integer supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        supplierRepository.delete(supplier);

        return new SuccessDTO("Supplier deleted successfully");
    }

    
    @Override
    public SuccessDTO assignPetToSupplier(Integer supplierId, Integer petId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        supplier.getPets().add(pet);

        supplierRepository.save(supplier);

        return new SuccessDTO("Pet assigned to supplier");
    }

    @Override
    public SuccessDTO removePetFromSupplier(Integer supplierId, Integer petId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

        supplier.getPets().remove(pet);

        supplierRepository.save(supplier);

        return new SuccessDTO("Pet removed from supplier");
    }

    @Override
    public List<PetResponseDTO> getPetsBySupplier(Integer supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        return supplier.getPets()
                .stream()
                .map(PetResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponseDTO> searchSupplierByName(String name) {

        return supplierRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(SupplierResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierResponseDTO> getSuppliersByCity(String city) {

        return supplierRepository.findByAddress_CityIgnoreCase(city)
                .stream()
                .map(SupplierResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}