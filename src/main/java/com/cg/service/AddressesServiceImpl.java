package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.AddressesRequestDTO;
import com.cg.dto.AddressesResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.entity.Addresses;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repo.AddressesRepository;

@Service
public class AddressesServiceImpl implements AddressesService {

    @Autowired
    private AddressesRepository addressesRepository;

    private AddressesResponseDTO convertToResponseDTO(Addresses address) {
        AddressesResponseDTO responseDTO = new AddressesResponseDTO();
        responseDTO.setAddressId(address.getAddressId());
        responseDTO.setStreet(address.getStreet());
        responseDTO.setCity(address.getCity());
        responseDTO.setState(address.getState());
        responseDTO.setZipCode(address.getZipCode());
        return responseDTO;
    }

    private Addresses convertToEntity(AddressesRequestDTO requestDTO) {
        Addresses address = new Addresses();
        address.setStreet(requestDTO.getStreet());
        address.setCity(requestDTO.getCity());
        address.setState(requestDTO.getState());
        address.setZipCode(requestDTO.getZipCode());
        return address;
    }

    private void updateEntityFromDTO(AddressesRequestDTO dto, Addresses address) {
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());
    }

    @Override
    public AddressesResponseDTO createAddress(AddressesRequestDTO requestDTO) {
        Addresses address = convertToEntity(requestDTO);
        Addresses savedAddress = addressesRepository.save(address);
        return convertToResponseDTO(savedAddress);
    }

    @Override
    public AddressesResponseDTO getAddressById(Integer addressId) {
        Addresses address = addressesRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", addressId));

        return convertToResponseDTO(address);
    }

    @Override
    public List<AddressesResponseDTO> getAllAddresses() {
        return addressesRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AddressesResponseDTO updateAddress(Integer addressId, AddressesRequestDTO requestDTO) {
        Addresses address = addressesRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", addressId));


        updateEntityFromDTO(requestDTO, address);
        Addresses updatedAddress = addressesRepository.save(address);
        return convertToResponseDTO(updatedAddress);
    }

    @Override
    public SuccessDTO deleteAddress(Integer addressId) {
        if (!addressesRepository.existsById(addressId)) {
            throw new ResourceNotFoundException("Address", addressId);

        }
        addressesRepository.deleteById(addressId);
        return new SuccessDTO("Address with ID " + addressId + " deleted successfully");
    }
}