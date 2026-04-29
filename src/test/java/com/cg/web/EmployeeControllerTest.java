//package com.cg.web;
//
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.cg.dto.EmployeeResponseDTO;
//import com.cg.dto.SuccessDTO;
//import com.cg.exception.ResourceNotFoundException;
//import com.cg.service.EmployeeService;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class EmployeeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private EmployeeService employeeService;
//
//    private EmployeeResponseDTO getEmployee() {
//        return new EmployeeResponseDTO(
//                1,
//                "John",
//                "john@gmail.com",
//                "Manager",
//                50000.0
//        );
//    }
//
//    @Test
//    @WithMockUser
//    public void testCreateEmployee() throws Exception {
//
//        Mockito.when(employeeService.createEmployee(Mockito.any()))
//                .thenReturn(new SuccessDTO("Employee created successfully"));
//
//        String json = """
//                {
//                  "name":"John",
//                  "email":"john@gmail.com",
//                  "role":"Manager",
//                  "salary":50000
//                }
//                """;
//
//        mockMvc.perform(post("/api/employees")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("Employee created successfully"));
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetAllEmployees() throws Exception {
//
//        Mockito.when(employeeService.getAllEmployees())
//                .thenReturn(List.of(getEmployee()));
//
//        mockMvc.perform(get("/api/employees"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("John"));
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetEmployeeById_success() throws Exception {
//
//        Mockito.when(employeeService.getEmployeeById(1))
//                .thenReturn(getEmployee());
//
//        mockMvc.perform(get("/api/employees/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("John"));
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetEmployeeById_notFound() throws Exception {
//
//        Mockito.when(employeeService.getEmployeeById(99))
//                .thenThrow(new ResourceNotFoundException("Employee not found"));
//
//        mockMvc.perform(get("/api/employees/99"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Employee not found"));
//    }
//
//    @Test
//    @WithMockUser
//    public void testUpdateEmployee() throws Exception {
//
//        Mockito.when(employeeService.updateEmployee(Mockito.eq(1), Mockito.any()))
//                .thenReturn(new SuccessDTO("Employee updated successfully"));
//
//        String json = """
//                {
//                  "name":"John Updated",
//                  "email":"john@gmail.com",
//                  "role":"Manager",
//                  "salary":60000
//                }
//                """;
//
//        mockMvc.perform(put("/api/employees/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Employee updated successfully"));
//    }
//
//    @Test
//    @WithMockUser
//    public void testDeleteEmployee() throws Exception {
//
//        Mockito.doNothing().when(employeeService).deleteEmployee(1);
//
//        mockMvc.perform(delete("/api/employees/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
//    }
//}