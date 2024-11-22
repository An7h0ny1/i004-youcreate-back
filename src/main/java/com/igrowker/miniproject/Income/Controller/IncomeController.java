package com.igrowker.miniproject.Income.Controller;

import com.igrowker.miniproject.Income.DTO.IncomeCreateRequestDTO;
import com.igrowker.miniproject.Income.DTO.IncomeEntityResponseDTO;
import com.igrowker.miniproject.Income.DTO.IncomeUpdateRequestDTO;
import com.igrowker.miniproject.Income.Service.IncomeService;
import com.igrowker.miniproject.Utils.Api_Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/income")
public class IncomeController {
    @Autowired
    private final IncomeService incomeService;

    @GetMapping("/{userId}")
    @Tag(name = "Income", description = "API for income data.")
    @Operation(summary = "Get Incomes", description = "Get all Incomes by user id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingresos encontrados para el usuario"),
            @ApiResponse(responseCode = "400", description = "El id del usuario debe ser mayor a 0 o los datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<List<IncomeEntityResponseDTO>>> getIncomes(@PathVariable Long userId) {
        List<IncomeEntityResponseDTO> response = incomeService.getIncomes(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Api_Response<>(response, "Ingresos encontrados correctamente", 200));
    }

    @GetMapping("/{id}")
    @Tag(name = "Income", description = "API for income data.")
    @Operation(summary = "Get Income", description = "Get income data by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingreso encontrado correctamente"),
            @ApiResponse(responseCode = "400", description = "El id del ingreso debe ser mayor a 0 o los datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Ingreso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<IncomeEntityResponseDTO>> getIncome(@PathVariable Long id) {
        IncomeEntityResponseDTO response = incomeService.getIncome(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Api_Response<>(response, "Ingreso encontrado correctamente", 200));
    }

    @PostMapping("/create")
    @Tag(name = "Income", description = "API for create income.")
    @Operation(summary = "Create Income", description = "Create a new income.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Colaborador creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<IncomeEntityResponseDTO>> createIncome(@RequestBody @Valid IncomeCreateRequestDTO incomeCreateRequestDTO) {
        IncomeEntityResponseDTO response = incomeService.createIncome(incomeCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Api_Response<>(response, "Ingreso creado correctamente", 201));
    }

    @PutMapping("/{id}")
    @Tag(name = "Income", description = "API for update income.")
    @Operation(summary = "Update Income", description = "Update a income.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingreso actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Ingreso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<IncomeEntityResponseDTO>> updateIncome(@PathVariable Long id, @RequestBody @Valid IncomeUpdateRequestDTO incomeUpdateRequestDTO) {
        IncomeEntityResponseDTO response = incomeService.updateIncome(id, incomeUpdateRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Api_Response<>(response, "Ingreso actualizado correctamente", 200));
    }

    @DeleteMapping("/{id}")
    @Tag(name = "Income", description = "API for delete incomes.")
    @Operation(summary = "Delete Income", description = "Delete a income.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingreso eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Ingreso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<IncomeEntityResponseDTO>> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Api_Response<>(null, "Ingreso eliminado correctamente", 200));
    }

}
