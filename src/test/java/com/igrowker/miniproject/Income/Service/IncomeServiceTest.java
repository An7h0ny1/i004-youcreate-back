package com.igrowker.miniproject.Income.Service;

import com.igrowker.miniproject.Income.DTO.IncomeCreateRequestDTO;
import com.igrowker.miniproject.Income.DTO.IncomeEntityResponseDTO;
import com.igrowker.miniproject.Income.DTO.IncomeUpdateRequestDTO;
import com.igrowker.miniproject.Income.Exception.IncomeNotFoundException;
import com.igrowker.miniproject.Income.Exception.InvalidIncomeFieldException;
import com.igrowker.miniproject.Income.Model.Income;
import com.igrowker.miniproject.Income.Repository.IncomeRepository;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IncomeServiceTest {

    @InjectMocks
    private IncomeService incomeService;

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetIncomes() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);

        Income income = new Income();
        income.setId(1L);
        income.setAmount(100.0);
        income.setOrigin("Salary");
        income.setDate(new Date());
        income.setUser (user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(incomeRepository.findByUserId(userId)).thenReturn(Collections.singletonList(income));

        List<IncomeEntityResponseDTO> incomes = incomeService.getIncomes(userId);

        assertEquals(1, incomes.size());
        assertEquals("Salary", incomes.get(0).origin());
        verify(userRepository).findById(userId);
        verify(incomeRepository).findByUserId(userId);
    }

    @Test
    public void testGetIncome_NotFound() {
        Long incomeId = 1L;

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IncomeNotFoundException.class, () -> {
            incomeService.getIncome(incomeId);
        });

        assertEquals("Ingreso con id " + incomeId + " no encontrado", exception.getMessage());
    }

    @Test
    public void testCreateIncome_Success() {
        IncomeCreateRequestDTO requestDTO = new IncomeCreateRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setOrigin("Salary");
        requestDTO.setDate(new Date());
        requestDTO.setUser_id(1L);

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findById(requestDTO.getUser_id())).thenReturn(Optional.of(user));
        when(incomeRepository.save(any(Income.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncomeEntityResponseDTO response = incomeService.createIncome(requestDTO);

        assertNotNull(response);
        assertEquals("Salary", response.origin());
        verify(incomeRepository).save(any(Income.class));
    }

    @Test
    public void testCreateIncome_UserNotFound() {
        IncomeCreateRequestDTO requestDTO = new IncomeCreateRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setOrigin("Salary");
        requestDTO.setDate(new Date());
        requestDTO.setUser_id(1L);

        when(userRepository.findById(requestDTO.getUser_id())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            incomeService.createIncome(requestDTO);
        });

        assertEquals("Usuario con id " + requestDTO.getUser_id() + " no encontrado", exception.getMessage());
    }

    @Test
    public void testCreateIncome_InvalidAmount() {
        IncomeCreateRequestDTO requestDTO = new IncomeCreateRequestDTO();
        requestDTO.setAmount(-100.0); // Monto inválido
        requestDTO.setOrigin("Salary");
        requestDTO.setDate(new Date());
        requestDTO.setUser_id(1L);

        Exception exception = assertThrows(InvalidIncomeFieldException.class, () -> {
            incomeService.createIncome(requestDTO);
        });

        assertEquals("El monto de ingreso debe ser mayor a 0", exception.getMessage());
    }

    @Test
    public void testDeleteIncome_Success() {
        Long incomeId = 1L;

        when(incomeRepository.existsById(incomeId)).thenReturn(true);

        incomeService.deleteIncome(incomeId);

        verify(incomeRepository).deleteById(incomeId);
    }

    @Test
    public void testDeleteIncome_NotFound() {
        Long incomeId = 1L;

        when(incomeRepository.existsById(incomeId)).thenReturn(false);

        Exception exception = assertThrows(IncomeNotFoundException.class, () -> {
            incomeService.deleteIncome(incomeId);
        });

        assertEquals("Ingreso con id " + incomeId + " no encontrado", exception.getMessage());
    }

    @Test
    public void testUpdateIncome_Success() {
        Long incomeId = 1L;

        Income existingIncome = new Income();
        existingIncome.setId(incomeId);
        existingIncome.setAmount(100.0);
        existingIncome.setOrigin("Salary");
        existingIncome.setDate(new Date());

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(existingIncome));

        IncomeUpdateRequestDTO updateRequest = new IncomeUpdateRequestDTO();
        updateRequest.setAmount(150.0);
        updateRequest.setOrigin("Bonus");
        updateRequest.setDate(new Date());

        when(incomeRepository.save(any(Income.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncomeEntityResponseDTO updatedIncomeResponse = incomeService.updateIncome(incomeId, updateRequest);

        assertNotNull(updatedIncomeResponse);
        assertEquals(150.0, updatedIncomeResponse.amount());
        assertEquals("Bonus", updatedIncomeResponse.origin());
        verify(incomeRepository).findById(incomeId);
        verify(incomeRepository).save(any(Income.class));
    }

    @Test
    public void testUpdateIncome_NotFound() {
        Long incomeId = 1L;

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());

        IncomeUpdateRequestDTO updateRequest = new IncomeUpdateRequestDTO();
        updateRequest.setAmount(150.0);
        updateRequest.setOrigin("Bonus");
        updateRequest.setDate(new Date());

        Exception exception = assertThrows(IncomeNotFoundException.class, () -> {
            incomeService.updateIncome(incomeId, updateRequest);
        });

        assertEquals("Ingreso con id " + incomeId + " no encontrado", exception.getMessage());
    }
}
