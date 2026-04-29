package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cg.dto.PetResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRequestDTO;
import com.cg.dto.SupplierResponseDTO;
import com.cg.entity.Addresses;
import com.cg.entity.Pet;
import com.cg.entity.Supplier;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressesRepository;
import com.cg.repo.PetRepository;
import com.cg.repo.RoleRepository;
import com.cg.repo.SupplierRepository;
import com.cg.repo.UserRepository;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AddressesRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Constructor
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               PetRepository petRepository,
                               AddressesRepository addressRepository) {
        this.supplierRepository = supplierRepository;
        this.petRepository = petRepository;
        this.addressRepository = addressRepository;
    }

	
    @Override
    public SupplierResponseDTO getSupplierById(Integer supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));

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
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));

        supplier.setName(dto.getName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setEmail(dto.getEmail());

        if (dto.getAddressId() != null) {
            Addresses address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new ResourceNotFoundException("Address", dto.getAddressId()));
            supplier.setAddress(address);
        }

        supplierRepository.save(supplier);

        return new SuccessDTO("Supplier updated successfully");
    }

    @Override
    public SuccessDTO deleteSupplier(Integer supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));

        supplierRepository.delete(supplier);

        return new SuccessDTO("Supplier deleted successfully");
    }

    @Override
    public SuccessDTO assignPetToSupplier(Integer supplierId, Integer petId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet", petId));

        supplier.getPets().add(pet);

        supplierRepository.save(supplier);

        return new SuccessDTO("Pet assigned to supplier");
    }

    @Override
    public SuccessDTO removePetFromSupplier(Integer supplierId, Integer petId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet", petId));

        supplier.getPets().remove(pet);

        supplierRepository.save(supplier);

        return new SuccessDTO("Pet removed from supplier");
    }

    @Override
    public List<PetResponseDTO> getPetsBySupplier(Integer supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", supplierId));

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