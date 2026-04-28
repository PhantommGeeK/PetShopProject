package com.cg.service;

import com.cg.dto.GroomingServicesRequestDTO;
import com.cg.dto.GroomingServicesResponseDTO;
import com.cg.dto.SuccessDTO;

import java.util.*;

public interface GroomingServicesService {
	SuccessDTO createService(GroomingServicesRequestDTO requestDTO);

    GroomingServicesResponseDTO getServiceById(int serviceId);

    List<GroomingServicesResponseDTO> getAllServices();

    List<GroomingServicesResponseDTO> getAvailableServices();

    SuccessDTO updateService(int serviceId, GroomingServicesRequestDTO requestDTO);

    GroomingServicesResponseDTO toggleAvailability(int serviceId);

    SuccessDTO deleteService(int serviceId);

}
