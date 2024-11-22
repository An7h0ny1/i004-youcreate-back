package com.igrowker.miniproject.Income.Service;

import com.igrowker.miniproject.Income.DTO.IncomeCreateRequestDTO;
import com.igrowker.miniproject.Income.DTO.IncomeEntityResponseDTO;
import com.igrowker.miniproject.Income.DTO.IncomeUpdateRequestDTO;
import com.igrowker.miniproject.Income.Exception.BadIncomeBodyRequestException;
import com.igrowker.miniproject.Income.Exception.IncomeNotFoundException;
import com.igrowker.miniproject.Income.Exception.InvalidIncomeFieldException;
import com.igrowker.miniproject.Income.Exception.InvalidIncomeIdException;
import com.igrowker.miniproject.Income.Model.Income;
import com.igrowker.miniproject.Income.Repository.IncomeRepository;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public List<IncomeEntityResponseDTO> getIncomes(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con id " + id + " no encontrado"));

        List<Income> collaborators = incomeRepository.findByUserId(id);

        return collaborators.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    public IncomeEntityResponseDTO getIncome(Long id) {
        validateIncomeId(id);

        return incomeRepository.findById(id)
                .map(income -> new IncomeEntityResponseDTO(
                        income.getId(),
                        income.getAmount(),
                        income.getOrigin(),
                        income.getDate(),
                        income.getUser().getId()
                ))
                .orElseThrow(() -> new IncomeNotFoundException("Ingreso con id " + id + " no encontrado"));
    }

    public IncomeEntityResponseDTO createIncome(IncomeCreateRequestDTO incomeCreateRequestDTO) {
        validateIncomeCreateFields(incomeCreateRequestDTO);

        UserEntity user = userRepository.findById(incomeCreateRequestDTO.getUser_id())
                .orElseThrow(() -> new UserNotFoundException("Usuario con id " + incomeCreateRequestDTO.getUser_id() + " no encontrado"));

        Income income = Income.builder()
                .amount(incomeCreateRequestDTO.getAmount())
                .origin(incomeCreateRequestDTO.getOrigin())
                .date(incomeCreateRequestDTO.getDate())
                .user(user)
                .build();

        incomeRepository.save(income);

        return new IncomeEntityResponseDTO(
                income.getId(),
                income.getAmount(),
                income.getOrigin(),
                income.getDate(),
                income.getUser().getId()
        );
    }

    public IncomeEntityResponseDTO updateIncome(Long id, IncomeUpdateRequestDTO incomeUpdateRequestDTO) {
        validateIncomeId(id);

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Colaborador con id " + id + " no encontrado"));

        validateIncomeUpdateFields(incomeUpdateRequestDTO);

        income.setAmount(incomeUpdateRequestDTO.getAmount());
        income.setOrigin(incomeUpdateRequestDTO.getOrigin());
        income.setDate(incomeUpdateRequestDTO.getDate());

        incomeRepository.save(income);

        return new IncomeEntityResponseDTO(
                income.getId(),
                income.getAmount(),
                income.getOrigin(),
                income.getDate(),
                income.getUser().getId()
        );
    }

    public void deleteIncome(Long id) {
        validateIncomeId(id);

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Ingreso con id " + id + " no encontrado"));

        incomeRepository.delete(income);
    }

    public IncomeEntityResponseDTO entityToDTO(Income income) {
        return new IncomeEntityResponseDTO(
                income.getId(),
                income.getAmount(),
                income.getOrigin(),
                income.getDate(),
                income.getUser().getId()
        );
    }

    public void validateIncomeCreateFields(IncomeCreateRequestDTO incomeCreateRequestDTO) {
        if (incomeCreateRequestDTO == null) {
            throw new BadIncomeBodyRequestException("El ingreso solicitado no puede ser nulo");
        }

        validateIncomeFields(incomeCreateRequestDTO.getAmount(), incomeCreateRequestDTO.getOrigin(), incomeCreateRequestDTO.getDate());
    }

    public void validateIncomeUpdateFields(IncomeUpdateRequestDTO incomeUpdateRequestDTO) {
        if (incomeUpdateRequestDTO == null) {
            throw new BadIncomeBodyRequestException("El ingreso solicitado no puede ser nulo");
        }

        validateIncomeFields(incomeUpdateRequestDTO.getAmount(), incomeUpdateRequestDTO.getOrigin(), incomeUpdateRequestDTO.getDate());
    }

    private void validateIncomeFields(Double amount, String origin, Date date) {
        if (amount <= 0) {
            throw new InvalidIncomeFieldException("El monto de ingreso debe ser mayor a 0");
        }

        if (origin == null || origin.isEmpty()) {
            throw new InvalidIncomeFieldException("El nombre origen del ingreso no puede estar vacío");
        }

        if (date == null) {
            throw new InvalidIncomeFieldException("El fecha del ingreso no puede estar vacío");
        }
    }

    public void validateIncomeId(Long id) {
        if (id <= 0) {
            throw new InvalidIncomeIdException("El id de ingreso debe ser mayor a 0");
        }
    }
}
