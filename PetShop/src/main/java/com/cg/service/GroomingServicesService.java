package com.cg.service;

import com.cg.dto.GroomingServiceRequestDTO;
import com.cg.dto.GroomingServiceResponseDTO;
import java.util.*;

public interface GroomingServicesService {
	GroomingServiceResponseDTO createService(GroomingServiceRequestDTO requestDTO);

    GroomingServiceResponseDTO getServiceById(int serviceId);

    List<GroomingServiceResponseDTO> getAllServices();

    List<GroomingServiceResponseDTO> getAvailableServices();

    GroomingServiceResponseDTO updateService(int serviceId, GroomingServiceRequestDTO requestDTO);

    GroomingServiceResponseDTO toggleAvailability(int serviceId);

    void deleteService(int serviceId);

}
