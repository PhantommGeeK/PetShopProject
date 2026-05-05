package com.cg.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cg.dto.AddressesResponseDTO;
import com.cg.dto.PetResponseDTO;
import com.cg.dto.SuccessDTO;
import com.cg.dto.SupplierRequestDTO;
import com.cg.dto.SupplierResponseDTO;
import com.cg.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class SupplierControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SupplierResponseDTO supplierResponseDTO;
    private SupplierRequestDTO supplierRequestDTO;
    private AddressesResponseDTO addressResponseDTO;
    private PetResponseDTO petResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(supplierController).build();

        addressResponseDTO = new AddressesResponseDTO();
        addressResponseDTO.setAddressId(1);
        addressResponseDTO.setStreet("MG Road");
        addressResponseDTO.setCity("Delhi");
        addressResponseDTO.setState("UP");
        addressResponseDTO.setZipCode("201001");

        supplierResponseDTO = new SupplierResponseDTO();
        supplierResponseDTO.setSupplierId(1);
        supplierResponseDTO.setName("Pet Supplies Co");
        supplierResponseDTO.setContactPerson("Rahul Kumar");
        supplierResponseDTO.setPhoneNumber("9876543210");
        supplierResponseDTO.setEmail("supplier@gmail.com");
        supplierResponseDTO.setAddress(addressResponseDTO);

        supplierRequestDTO = new SupplierRequestDTO();
        supplierRequestDTO.setName("Pet Supplies Co");
        supplierRequestDTO.setContactPerson("Rahul Kumar");
        supplierRequestDTO.setPhoneNumber("9876543210");
        supplierRequestDTO.setEmail("supplier@gmail.com");
        supplierRequestDTO.setAddressId(1);

        petResponseDTO = new PetResponseDTO();
        petResponseDTO.setPetId(10);
        petResponseDTO.setName("Bruno");
        petResponseDTO.setBreed("Labrador");
        petResponseDTO.setAge(2);
        petResponseDTO.setPrice(15000.0);
    }

    @Test
    void testGetSuppliers_success() throws Exception {
        List<SupplierResponseDTO> suppliers = Arrays.asList(supplierResponseDTO);
        when(supplierService.getAllSuppliers()).thenReturn(suppliers);

        mockMvc.perform(get("/api/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].supplierId").value(1))
                .andExpect(jsonPath("$[0].name").value("Pet Supplies Co"))
                .andExpect(jsonPath("$[0].email").value("supplier@gmail.com"));
    }

    @Test
    void testGetSupplierById_success() throws Exception {
        when(supplierService.getSupplierById(1)).thenReturn(supplierResponseDTO);

        mockMvc.perform(get("/api/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplierId").value(1))
                .andExpect(jsonPath("$.name").value("Pet Supplies Co"))
                .andExpect(jsonPath("$.contactPerson").value("Rahul Kumar"))
                .andExpect(jsonPath("$.phoneNumber").value("9876543210"))
                .andExpect(jsonPath("$.address.city").value("Delhi"));
    }

    @Test
    void testGetPetsBySupplier_success() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponseDTO);
        when(supplierService.getPetsBySupplier(1)).thenReturn(pets);

        mockMvc.perform(get("/api/suppliers/1/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].petId").value(10))
                .andExpect(jsonPath("$[0].name").value("Bruno"))
                .andExpect(jsonPath("$[0].breed").value("Labrador"));
    }

    @Test
    void testUpdateSupplier_success() throws Exception {
        when(supplierService.updateSupplier(eq(1), any(SupplierRequestDTO.class)))
                .thenReturn(new SuccessDTO("Supplier with ID 1 updated successfully"));

        mockMvc.perform(put("/api/suppliers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Supplier with ID 1 updated successfully"));
    }

    @Test
    void testDeleteSupplier_success() throws Exception {
        when(supplierService.deleteSupplier(1))
                .thenReturn(new SuccessDTO("Supplier with ID 1 deleted successfully"));

        mockMvc.perform(delete("/api/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Supplier with ID 1 deleted successfully"));
    }

    @Test
    void testAssignPetToSupplier_success() throws Exception {
        when(supplierService.assignPetToSupplier(1, 10))
                .thenReturn(new SuccessDTO("Pet assigned to supplier successfully"));

        mockMvc.perform(post("/api/suppliers/1/pets/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Pet assigned to supplier successfully"));
    }

    @Test
    void testRemovePetFromSupplier_success() throws Exception {
        when(supplierService.removePetFromSupplier(1, 10))
                .thenReturn(new SuccessDTO("Pet removed from supplier successfully"));

        mockMvc.perform(delete("/api/suppliers/1/pets/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Pet removed from supplier successfully"));
    }

    @Test
    void testSearchSupplierByName_success() throws Exception {
        List<SupplierResponseDTO> suppliers = Arrays.asList(supplierResponseDTO);
        when(supplierService.searchSupplierByName("Pet Supplies Co"))
                .thenReturn(suppliers);

        mockMvc.perform(get("/api/suppliers/search")
                .param("name", "Pet Supplies Co"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pet Supplies Co"));
    }

    @Test
    void testGetSuppliersByCity_success() throws Exception {
        List<SupplierResponseDTO> suppliers = Arrays.asList(supplierResponseDTO);
        when(supplierService.getSuppliersByCity("Delhi")).thenReturn(suppliers);

        mockMvc.perform(get("/api/suppliers/city")
                .param("city", "Delhi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].address.city").value("Delhi"));
    }
}