package com.cg.service;

import java.util.List;

import com.cg.dto.PetResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRequestDTO;
import com.cg.dto.SupplierResponseDTO;

public interface SupplierService 
{
	public SuccessDTO addSupplier(SupplierRequestDTO dto);
	public SupplierResponseDTO getSupplierById(Integer supplierId);
	public List<SupplierResponseDTO> getAllSuppliers();
	public List<PetResponseDTO> getPetsBySupplier(Integer supplierId);
	public SuccessDTO updateSupplier(Integer supplierId, SupplierRequestDTO dto);
	public SuccessDTO deleteSupplier(Integer supplierId);
	public SuccessDTO assignPetToSupplier(Integer supplierId, Integer petId);
    public SuccessDTO removePetFromSupplier(Integer supplierId, Integer petId);

    public List<SupplierResponseDTO> searchSupplierByName(String name);
    public List<SupplierResponseDTO> getSuppliersByCity(String city);
}
