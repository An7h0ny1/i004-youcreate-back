package com.igrowker.miniproject.TaxObligation.Controller;

import com.igrowker.miniproject.TaxObligation.Dto.TaxCategory;
import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.TaxObligation.Service.TaxNotificationService;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/taxes")
public class TaxController {


    private final TaxService taxService;
    private final TaxNotificationService taxNotificationService;
    private final UserRepository userRepository;

    public TaxController(TaxService taxService, TaxNotificationService taxNotificationService, UserRepository userRepository) {
        this.taxService = taxService;
        this.taxNotificationService = taxNotificationService;
        this.userRepository = userRepository;
    }


    @Operation(
            summary = "Obtiene todos los impuestos y estados por usuario",
            description = "Lista tipo de impuestos, fechas y estado de un usuario"
    )
    @ApiResponse(responseCode = "200", description = "Lista de Obligaciones fiscales")
    @GetMapping("/taxes/{userId}")
    public ResponseEntity<List<TaxDTO>> getTaxesForUser(@PathVariable Long userId) {
        List<TaxDTO> taxes = taxService.getTaxesForUser(userId);
        return ResponseEntity.ok(taxes);
    }

    @Operation(
            summary = "Asociar Impuestos Manualmente",
            description = "Asocia tipos de impuesto y fechas de vencimineto limite para un usuario"
    )
    @ApiResponse(responseCode = "200", description = "Asociación exitosa")
    @PostMapping("/associate/{userId}")
    public ResponseEntity<String> associateTaxes(@PathVariable Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        taxService.associateTaxesToUser(user);
        return ResponseEntity.ok("Impuestos asociados exitosamente.");
    }

    @Operation(
            summary = "Envio Notificacion manualmente",
            description = "Se envia una notificacions sobre fechas proximas de impuestos"
    )
    @ApiResponse(responseCode = "200", description = "Notificacion Enviada.")
    @GetMapping("/notifications/send")
    public ResponseEntity<String> triggerNotifications() {
        taxService.sendDailyTaxNotifications();
        return ResponseEntity.ok("Notificaciones activadas.");
    }

    @Operation(
            summary = "Calcular impuestos para un usuario",
            description = "Obtiene detalles de ingresos de un usuario y calcula los impuestos aplicables según su país."
    )
    @ApiResponse(responseCode = "200", description = "Impuestos calculados correctamente.")
    @GetMapping("/calculateTaxes/{userId}")
    public ResponseEntity<List<TaxDTO>> calculateTaxes(@PathVariable Long userId) {
        List<TaxDTO> taxes = taxService.calculateTaxes(userId);
        return ResponseEntity.ok(taxes);
    }

    @PostMapping("/pay-vat")
    public ResponseEntity<String> payVAT(@RequestParam Long userId) {
        try {
            taxService.payVAT(userId);
            return ResponseEntity.ok("VAT paid successfully.");
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/pay-isr")
    public ResponseEntity<String> payISR(@RequestParam Long userId) {
        try {
            taxService.payISR(userId);
            return ResponseEntity.ok("ISR paid successfully.");
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /*
    @GetMapping("/total-payments")
    public ResponseEntity<String> getTotalPayments(@RequestParam Long userId) {
        double totalPayments = taxService.calculateTotalPayments(userId);
        return ResponseEntity.ok("Total payments made: " + totalPayments);
    }

     */
}
