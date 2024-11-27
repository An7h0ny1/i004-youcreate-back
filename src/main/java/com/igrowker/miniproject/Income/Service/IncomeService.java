package com.igrowker.miniproject.Income.Service;

import com.igrowker.miniproject.Collaborator.Repository.CollaboratorRepository;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeService {

    private  final IncomeRepository incomeRepository;
    private  final UserRepository userRepository;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    private Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Fecha inválida: " + dateString);
        }
    }

    private void validarCategoria(String category) {
        if (!category.equals("campaña") && !category.equals("colaboracion")) {
            throw new IllegalArgumentException("Categoría inválida");
        }
    }

    public List<IncomeEntityResponseDTO> getIncomes(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario con id " + id + " no encontrado"));

        List<Income> collaborators = incomeRepository.findByUserId(id);

        return collaborators.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    public IncomeEntityResponseDTO getIncome(Long id) {
        //validateIncomeId(id);

        return incomeRepository.findById(id)
                .map(income -> new IncomeEntityResponseDTO(
                        income.getId(),
                        income.getAmount(),
                        income.getOrigin(),
                        income.getDate(),
                        income.getCategory(),
                        income.getUser().getId()
                ))
                .orElseThrow(() -> new IncomeNotFoundException("Ingreso con id " + id + " no encontrado"));
    }

    public IncomeEntityResponseDTO createIncome(IncomeCreateRequestDTO incomeCreateRequestDTO) {
        validateIncomeCreateFields(incomeCreateRequestDTO);

        validarCategoria(incomeCreateRequestDTO.getCategory());

        UserEntity user = userRepository.findById(incomeCreateRequestDTO.getUser_id())
                .orElseThrow(() -> new UserNotFoundException("Usuario con id " + incomeCreateRequestDTO.getUser_id() + " no encontrado"));

        // Convertir la fecha de String a Date
        Date date = convertStringToDate(incomeCreateRequestDTO.getDate());

        Income income = Income.builder()
                .amount(incomeCreateRequestDTO.getAmount())
                .origin(incomeCreateRequestDTO.getOrigin())
                .date(incomeCreateRequestDTO.getDate())
                .category(incomeCreateRequestDTO.getCategory())
                .user(user)
                .build();

        incomeRepository.save(income);

        return new IncomeEntityResponseDTO(
                income.getId(),
                income.getAmount(),
                income.getOrigin(),
                income.getDate(),
                income.getCategory(),
                income.getUser().getId()
        );
    }

    public IncomeEntityResponseDTO updateIncome(Long id, IncomeUpdateRequestDTO incomeUpdateRequestDTO) {
        //validateIncomeId(id);

        validarCategoria(incomeUpdateRequestDTO.getCategory());

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Ingreso con id " + id + " no encontrado"));

        validateIncomeUpdateFields(incomeUpdateRequestDTO);

        Date date = convertStringToDate(incomeUpdateRequestDTO.getDate());


        income.setId(incomeUpdateRequestDTO.getId());
        income.setAmount(incomeUpdateRequestDTO.getAmount());
        income.setOrigin(incomeUpdateRequestDTO.getOrigin());
        income.setDate(incomeUpdateRequestDTO.getDate());
        income.setCategory(incomeUpdateRequestDTO.getCategory());

        incomeRepository.save(income);

        return new IncomeEntityResponseDTO(
                income.getId(),
                income.getAmount(),
                income.getOrigin(),
                income.getDate(),
                income.getCategory(),
                income.getUser().getId()
        );
    }

    public void deleteIncome(Long id) {
        //validateIncomeId(id);

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Ingreso con id " + id + " no encontrado"));

        incomeRepository.deleteById(id);
    }

    public IncomeEntityResponseDTO entityToDTO(Income income) {

        Date date = convertStringToDate(income.getDate());


        return new IncomeEntityResponseDTO(
                income.getId(),
                income.getAmount(),
                income.getOrigin(),
                income.getDate(),
                income.getCategory(),
                income.getUser().getId()
        );
    }

    public void validateIncomeCreateFields(IncomeCreateRequestDTO incomeCreateRequestDTO) {
        if (incomeCreateRequestDTO == null) {
            throw new BadIncomeBodyRequestException("El ingreso solicitado no puede ser nulo");
        }
        Date date = convertStringToDate(incomeCreateRequestDTO.getDate());

        validateIncomeFields(incomeCreateRequestDTO.getAmount(), incomeCreateRequestDTO.getOrigin(), date);
    }

    public void validateIncomeUpdateFields(IncomeUpdateRequestDTO incomeUpdateRequestDTO) {
        if (incomeUpdateRequestDTO == null) {
            throw new BadIncomeBodyRequestException("El ingreso solicitado no puede ser nulo");
        }

        Date date = convertStringToDate(incomeUpdateRequestDTO.getDate());

        validateIncomeFields(incomeUpdateRequestDTO.getAmount(), incomeUpdateRequestDTO.getOrigin(), date);
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

    public List<IncomeEntityResponseDTO> getIncomesByMonth(Long userId, int month, int year) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con id " + userId + " no encontrado"));

        List<Income> incomes = incomeRepository.findByUserId(userId);

        return incomes.stream()
                .filter(income -> {
                    //Date incomeDate = convertStringToDate(income.getDate());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(convertStringToDate(income.getDate()));
                    return calendar.get(Calendar.MONTH) + 1 == month && calendar.get(Calendar.YEAR) == year; // +1 porque los meses en Calendar son 0-indexados
                })
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

//    public void validateIncomeId(Long id) {
//        if (id <= 0) {
//            throw new InvalidIncomeIdException("El id de ingreso debe ser mayor a 0");
//        }
//    }
}
