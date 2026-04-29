package com.cg.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.dto.AddressesRequestDTO;
import com.cg.dto.AddressesResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.Addresses;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressesRepository;

@ExtendWith(MockitoExtension.class)
public class AddressesServiceTest {

    @Mock
    private AddressesRepository addressesRepository;

    @InjectMocks
    private AddressesService service = new AddressesServiceImpl();

    private Addresses address;
    private AddressesRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        address = new Addresses();
        address.setAddressId(1);
        address.setStreet("MG Road");
        address.setCity("Delhi");
        address.setState("UP");
        address.setZipCode("201001");

        requestDTO = new AddressesRequestDTO();
        requestDTO.setStreet("MG Road");
        requestDTO.setCity("Delhi");
        requestDTO.setState("UP");
        requestDTO.setZipCode("201001");
    }

    @Test
    void testCreateAddress_success() {

        Mockito.when(addressesRepository.save(any(Addresses.class))).thenReturn(address);

        AddressesResponseDTO result = service.createAddress(requestDTO);

        assertEquals(1, result.getAddressId());
        assertEquals("MG Road", result.getStreet());
        assertEquals("Delhi", result.getCity());
        assertEquals("UP", result.getState());
        assertEquals("201001", result.getZipCode());
        verify(addressesRepository, times(1)).save(any(Addresses.class));
    }
    
    @Test
    void testGetAddressById_success() {

        when(addressesRepository.findById(1)).thenReturn(Optional.of(address));

        AddressesResponseDTO result = service.getAddressById(1);

        assertEquals(1, result.getAddressId());
        assertEquals("MG Road", result.getStreet());
        verify(addressesRepository, times(1)).findById(1);
    }

    @Test
    void testGetAddressById_notFound() {

        when(addressesRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getAddressById(99);
        });
    }

    @Test
    void testGetAllAddresses_success() {

        Addresses address2 = new Addresses();
        address2.setAddressId(2);
        address2.setStreet("FC Road");
        address2.setCity("Pune");
        address2.setState("MH");
        address2.setZipCode("411001");

        when(addressesRepository.findAll()).thenReturn(Arrays.asList(address, address2));

        List<AddressesResponseDTO> result = service.getAllAddresses();

        assertEquals(2, result.size());
        assertEquals("MG Road", result.get(0).getStreet());
        assertEquals("FC Road", result.get(1).getStreet());
        verify(addressesRepository, times(1)).findAll();
    }

    @Test
    void testGetAllAddresses_emptyList() {

        when(addressesRepository.findAll()).thenReturn(Arrays.asList());

        List<AddressesResponseDTO> result = service.getAllAddresses();

        assertEquals(0, result.size());
        verify(addressesRepository, times(1)).findAll();
    }

    @Test
    void testUpdateAddress_success() {

        AddressesRequestDTO updateRequest = new AddressesRequestDTO();
        updateRequest.setStreet("New Street");
        updateRequest.setCity("Mumbai");
        updateRequest.setState("MH");
        updateRequest.setZipCode("400001");

        Addresses updatedAddress = new Addresses();
        updatedAddress.setAddressId(1);
        updatedAddress.setStreet("New Street");
        updatedAddress.setCity("Mumbai");
        updatedAddress.setState("MH");
        updatedAddress.setZipCode("400001");

        when(addressesRepository.findById(1)).thenReturn(Optional.of(address));
        when(addressesRepository.save(any(Addresses.class))).thenReturn(updatedAddress);

        AddressesResponseDTO result = service.updateAddress(1, updateRequest);

        assertEquals("New Street", result.getStreet());
        assertEquals("Mumbai", result.getCity());
        verify(addressesRepository, times(1)).findById(1);
        verify(addressesRepository, times(1)).save(any(Addresses.class));
    }

    @Test
    void testUpdateAddress_notFound() {

        when(addressesRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.updateAddress(99, requestDTO);
        });
    }

    @Test
    void testDeleteAddress_success() {

        when(addressesRepository.existsById(1)).thenReturn(true);

        SuccessDTO result = service.deleteAddress(1);

        assertEquals("Address with ID 1 deleted successfully", result.getMessage());
        verify(addressesRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAddress_notFound() {

        when(addressesRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            service.deleteAddress(99);
        });
    }
}