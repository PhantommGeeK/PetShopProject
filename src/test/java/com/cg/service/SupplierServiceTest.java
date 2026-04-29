package com.cg.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRequestDTO;
import com.cg.dto.SupplierResponseDTO;
import com.cg.entity.Addresses;
import com.cg.entity.Pet;
import com.cg.entity.Supplier;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressesRepository;
import com.cg.repo.PetRepository;
import com.cg.repo.SupplierRepository;

import java.util.ArrayList;
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
public class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private AddressesRepository addressRepository;

    @InjectMocks
    private SupplierServiceImpl service;

    private Supplier supplier;
    private Addresses address;
    private Pet pet;
    private SupplierRequestDTO requestDTO;

    @BeforeEach
    void setup() {

        address = new Addresses();
        address.setAddressId(1);
        address.setStreet("MG Road");
        address.setCity("Delhi");
        address.setState("UP");
        address.setZipCode("201001");

        pet = new Pet();
        pet.setPetId(1);
        pet.setName("Bruno");
        pet.setBreed("Labrador");
        pet.setAge(2);
        pet.setPrice(15000.0);

        supplier = new Supplier();
        supplier.setSupplierId(1);
        supplier.setName("John Suppliers");
        supplier.setContactPerson("John Doe");
        supplier.setPhoneNumber("9876543210");
        supplier.setEmail("johndoe@gmail.com");
        supplier.setAddress(address);
        supplier.setPets(new ArrayList<>());

        requestDTO = new SupplierRequestDTO();
        requestDTO.setName("John Suppliers");
        requestDTO.setContactPerson("John Doe");
        requestDTO.setPhoneNumber("9876543210");
        requestDTO.setEmail("johndoe@gmail.com");
        requestDTO.setAddressId(1);
    }

    @Test
    void testAddSupplier_success() {

        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        Mockito.when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SuccessDTO result = service.addSupplier(requestDTO);

        assertEquals("Supplier added successfully", result.getMessage());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testAddSupplier_addressNotFound_throwsException() {

        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.addSupplier(requestDTO);
        });
    }

    @Test
    void testGetSupplierById_success() {

        when(supplierRepository.findById(1)).thenReturn(Optional.of(supplier));

        SupplierResponseDTO result = service.getSupplierById(1);

        assertEquals(1, result.getSupplierId());
        assertEquals("John Suppliers", result.getName());
        assertEquals("John Doe", result.getContactPerson());
        assertEquals("johndoe@gmail.com", result.getEmail());
        verify(supplierRepository, times(1)).findById(1);
    }

    @Test
    void testGetSupplierById_notFound() {

        when(supplierRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getSupplierById(99);
        });
    }

    @Test
    void testGetAllSuppliers_success() {

        Supplier supplier2 = new Supplier();
        supplier2.setSupplierId(2);
        supplier2.setName("Doe Traders");
        supplier2.setContactPerson("Jane Doe");
        supplier2.setPhoneNumber("9876543211");
        supplier2.setEmail("janedoe@gmail.com");
        supplier2.setAddress(address);
        supplier2.setPets(new ArrayList<>());

        when(supplierRepository.findAll()).thenReturn(Arrays.asList(supplier, supplier2));

        List<SupplierResponseDTO> result = service.getAllSuppliers();

        assertEquals(2, result.size());
        assertEquals("John Suppliers", result.get(0).getName());
        assertEquals("Doe Traders", result.get(1).getName());
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    void testGetAllSuppliers_emptyList() {

        when(supplierRepository.findAll()).thenReturn(Arrays.asList());

        List<SupplierResponseDTO> result = service.getAllSuppliers();

        assertEquals(0, result.size());
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    void testUpdateSupplier_success() {

        when(supplierRepository.findById(1)).thenReturn(Optional.of(supplier));
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SuccessDTO result = service.updateSupplier(1, requestDTO);

        assertEquals("Supplier updated successfully", result.getMessage());
        verify(supplierRepository, times(1)).findById(1);
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testUpdateSupplier_notFound() {

        when(supplierRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.updateSupplier(99, requestDTO);
        });
    }

    @Test
    void testDeleteSupplier_success() {

        when(supplierRepository.findById(1)).thenReturn(Optional.of(supplier));

        SuccessDTO result = service.deleteSupplier(1);

        assertEquals("Supplier deleted successfully", result.getMessage());
        verify(supplierRepository, times(1)).delete(supplier);
    }

    @Test
    void testDeleteSupplier_notFound() {

        when(supplierRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteSupplier(99);
        });
    }

    @Test
    void testAssignPetToSupplier_success() {

        when(supplierRepository.findById(1)).thenReturn(Optional.of(supplier));
        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SuccessDTO result = service.assignPetToSupplier(1, 1);

        assertEquals("Pet assigned to supplier", result.getMessage());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testAssignPetToSupplier_supplierNotFound() {

        when(supplierRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.assignPetToSupplier(99, 1);
        });
    }

    @Test
    void testAssignPetToSupplier_petNotFound() {

        when(supplierRepository.findById(1)).thenReturn(Optional.of(supplier));
        when(petRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.assignPetToSupplier(1, 99);
        });
    }

    @Test
    void testRemovePetFromSupplier_success() {

        supplier.getPets().add(pet);

        when(supplierRepository.findById(1)).thenReturn(Optional.of(supplier));
        when(petRepository.findById(1)).thenReturn(Optional.of(pet));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);

        SuccessDTO result = service.removePetFromSupplier(1, 1);

        assertEquals("Pet removed from supplier", result.getMessage());
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }

    @Test
    void testSearchSupplierByName_success() {

        when(supplierRepository.findByNameContainingIgnoreCase("John"))
                .thenReturn(Arrays.asList(supplier));

        List<SupplierResponseDTO> result = service.searchSupplierByName("John");

        assertEquals(1, result.size());
        assertEquals("John Suppliers", result.get(0).getName());
        verify(supplierRepository, times(1)).findByNameContainingIgnoreCase("John");
    }

    @Test
    void testGetSuppliersByCity_success() {

        when(supplierRepository.findByAddress_CityIgnoreCase("Delhi"))
                .thenReturn(Arrays.asList(supplier));

        List<SupplierResponseDTO> result = service.getSuppliersByCity("Delhi");

        assertEquals(1, result.size());
        assertEquals("John Suppliers", result.get(0).getName());
        verify(supplierRepository, times(1)).findByAddress_CityIgnoreCase("Delhi");
    }
}