package com.igrowker.miniproject.Income.Controller;
/*
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igrowker.miniproject.Income.DTO.IncomeCreateRequestDTO;
import com.igrowker.miniproject.Income.DTO.IncomeEntityResponseDTO;
import com.igrowker.miniproject.Income.DTO.IncomeUpdateRequestDTO;
import com.igrowker.miniproject.Income.Exception.IncomeNotFoundException;
import com.igrowker.miniproject.Income.Exception.InvalidIncomeFieldException;
import com.igrowker.miniproject.Income.Model.Income;
import com.igrowker.miniproject.Income.Repository.IncomeRepository;
import com.igrowker.miniproject.Income.Service.IncomeService;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncomeController.class)
@WithMockUser
public class IncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncomeService incomeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private IncomeController incomeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetIncomes() throws Exception {
        List<IncomeEntityResponseDTO> incomes = Collections.singletonList(new IncomeEntityResponseDTO(1L, 100.0, "Salary", "2024-05-06", "campaña",1L));

        when(incomeService.getIncomes(1L)).thenReturn(incomes);

        mockMvc.perform(get("/api/income/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].origin").value("Salary"));
    }

    @Test
    public void testCreateIncome()
    throws Exception {
        IncomeCreateRequestDTO requestDTO = new IncomeCreateRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setOrigin("Salary");
        requestDTO.setDate("2024-05-06");
        requestDTO.setUser_id(1L);

        IncomeEntityResponseDTO responseDTO = new IncomeEntityResponseDTO(1L, 100.0, "Salary", "2024-05-06", "campaña", 1L);

        when(incomeService.createIncome(any(IncomeCreateRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/income/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.0,\"origin\":\"Salary\",\"date\":\"2023-10-01\",\"user_id\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.origin").value("Salary"));
    }

    @Test
    public void testGetIncome_NotFound() throws Exception {
        Long incomeId = 1L;

        when(incomeService.getIncome(incomeId)).thenThrow(new IncomeNotFoundException("Ingreso con id " + incomeId + " no encontrado"));

        mockMvc.perform(get("/api/income/" + incomeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Ingreso con id " + incomeId + " no encontrado")));
    }

    @Test
    public void testCreateIncome_Success() throws Exception {
        IncomeCreateRequestDTO requestDTO = new IncomeCreateRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setOrigin("Salary");
        requestDTO.setDate("2024-05-06");
        requestDTO.setUser_id(1L);

        IncomeEntityResponseDTO responseDTO = new IncomeEntityResponseDTO(1L, 1000,"Salary", requestDTO.getDate(), "campaña",1L);

        when(incomeService.createIncome(any(IncomeCreateRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1L,\"amount\":100.0,\"origin\":\"Salary\",\"date\":\"" + requestDTO.getDate() + "\",\"user_id\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.date").value("2024-05-06"))
                .andExpect(jsonPath("$.origin").value("Salary"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.user_id").value(1L));

        verify(incomeService).createIncome(any(IncomeCreateRequestDTO.class));
    }

    @Test
    public void testCreateIncome_UserNotFound() throws Exception {
        IncomeCreateRequestDTO requestDTO = new IncomeCreateRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setOrigin("Salary");
        requestDTO.setDate("2024-05-06");
        requestDTO.setUser_id(1L);

        // Simular que el servicio lanza una excepción cuando el usuario no se encuentra
        doThrow(new UserNotFoundException("Usuario con id " + requestDTO.getUser_id() + " no encontrado"))
                .when(incomeService).createIncome(any(IncomeCreateRequestDTO.class));

        // Realizar la petición y verificar que se devuelve el estado 404 Not Found
        mockMvc.perform(post("/incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.0,\"origin\":\"Salary\",\"date\":\"" + requestDTO.getDate() + "\",\"user_id\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario con id " + requestDTO.getUser_id() + " no encontrado"));
    }

    @Test
    public void testCreateIncome_InvalidAmount() throws Exception {
        IncomeCreateRequestDTO requestDTO = new IncomeCreateRequestDTO();
        requestDTO.setAmount(-100.0); // Monto inválido
        requestDTO.setOrigin("Salary");
        requestDTO.setDate("2024-05-06");
        requestDTO.setUser_id(1L);

        // Simular que el servicio lanza una excepción cuando el monto es inválido
        doThrow(new InvalidIncomeFieldException("El monto debe ser mayor que cero"))
                .when(incomeService).createIncome(any(IncomeCreateRequestDTO.class));

        // Realizar la petición y verificar que se devuelve el estado 400 Bad Request
        mockMvc.perform(post("/incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":-100.0,\"origin\":\"Salary\",\"date\":\"" + requestDTO.getDate() + "\",\"user_id\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El monto debe ser mayor que cero"));
    }

    @Test
    public void testDeleteIncome_Success() throws Exception {
        Long incomeId = 1L;

        // Simular que el ingreso existe
        when(incomeRepository.existsById(incomeId)).thenReturn(true);

        // Realizar la petición DELETE
        mockMvc.perform(delete("/incomes/{id}", incomeId))
                .andExpect(status().isNoContent()); // 204 No Content

        // Verificar que el servicio fue llamado para eliminar el ingreso
        verify(incomeService).deleteIncome(incomeId);
    }

    @Test
    public void testDeleteIncome_NotFound() throws Exception {
        Long incomeId = 1L;

        // Simular que el ingreso no existe
        when(incomeRepository.existsById(incomeId)).thenReturn(false);

        // Simular que el servicio lanza una excepción al intentar eliminar un ingreso que no existe
        doThrow(new IncomeNotFoundException("Ingreso con id " + incomeId + " no encontrado"))
                .when(incomeService).deleteIncome(incomeId);

        // Realizar la petición DELETE
        mockMvc.perform(delete("/incomes/{id}", incomeId))
                .andExpect(status().isNotFound()) // 404 Not Found
                .andExpect(jsonPath("$.message").value("Ingreso con id " + incomeId + " no encontrado"));
    }

    @Test
    public void testUpdateIncome_Success() throws Exception {
        Long incomeId = 1L;

        Income existingIncome = new Income();
        existingIncome.setId(incomeId);
        existingIncome.setAmount(100.0);
        existingIncome.setOrigin("Salary");
        existingIncome.setDate("2024-05-06");

        // Simular que el ingreso existe
        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(existingIncome));

        IncomeUpdateRequestDTO updateRequest = new IncomeUpdateRequestDTO();
        updateRequest.setAmount(150.0);
        updateRequest.setOrigin("Bonus");
        updateRequest.setDate("2024-05-06");

        // Simular que el servicio devuelve el ingreso actualizado
        IncomeEntityResponseDTO updatedIncomeResponse = new IncomeEntityResponseDTO(1L, 150.0, "Bonus", "2024-05-06", "campaña",1L);

        when(incomeService.updateIncome(eq(incomeId), any(IncomeUpdateRequestDTO.class)))
                .thenReturn(updatedIncomeResponse);

        // Realizar la petición PUT
        mockMvc.perform(put("/incomes/{id}", incomeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(jsonPath("$.amount").value(150.0))
                .andExpect(jsonPath("$.origin").value("Bonus"));

        // Verificar que el servicio fue llamado con los parámetros correctos
        verify(incomeService).updateIncome(eq(incomeId), any(IncomeUpdateRequestDTO.class));
    }

    @Test
    public void testUpdateIncome_NotFound() throws Exception {
        Long incomeId = 1L;

        // Simular que el ingreso no existe
        when(incomeService.updateIncome(eq(incomeId), any(IncomeUpdateRequestDTO.class)))
                .thenThrow(new IncomeNotFoundException("Ingreso con id " + incomeId + " no encontrado"));

        IncomeUpdateRequestDTO updateRequest = new IncomeUpdateRequestDTO();
        updateRequest.setAmount(150.0);
        updateRequest.setOrigin("Bonus");
        updateRequest.setDate("2024-05-06");

        // Realizar la petición PUT y verificar que se devuelve un error 404
        mockMvc.perform(put("/incomes/{id}", incomeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound()) // 404 Not Found
                .andExpect(jsonPath("$.message").value("Ingreso con id " + incomeId + " no encontrado")); // Verificar el mensaje
    }
}
*/