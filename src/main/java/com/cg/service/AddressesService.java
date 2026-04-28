package com.cg.service;

import java.util.List;
import com.cg.dto.AddressesRequestDTO;
import com.cg.dto.AddressesResponseDTO;

public interface AddressesService {
    AddressesResponseDTO createAddress(AddressesRequestDTO requestDTO);
    AddressesResponseDTO getAddressById(Integer addressId);
    List<AddressesResponseDTO> getAllAddresses();
    AddressesResponseDTO updateAddress(Integer addressId, AddressesRequestDTO requestDTO);
    SuccessDTO deleteAddress(Integer addressId);
}