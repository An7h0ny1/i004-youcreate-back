package com.igrowker.miniproject.TaxObligation.Controller;

import com.igrowker.miniproject.TaxObligation.Dto.TaxCategory;
import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Service.TaxNotificationService;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/taxes")
public class TaxController {

    private final TaxService taxService;
    private final UserRepository userRepository;

    public TaxController(TaxService taxService, UserRepository userRepository) {
        this.taxService = taxService;
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

    @Operation(
            summary = "Pagar el impuesto IVA",
            description = "Paga el impuesto de tipo IVA y los campos se actualizan, el Estado y el Monto Pagado"
    )
    @ApiResponse(responseCode = "200", description = "Impuestos calculados correctamente.")
    @PostMapping("/pay-vat")
    public ResponseEntity<String> payVAT(@RequestParam Long userId) {
        try {
            taxService.paySpecificTax(userId, TaxCategory.IVA);
            return ResponseEntity.ok("Impuesto IVA pagado exitosamente.");
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(
            summary = "Pagar el impuesto ISR",
            description = "Paga el impuesto de tipo ISR y los campos se actualizan, el Estado y el Monto Pagado"
    )
    @ApiResponse(responseCode = "200", description = "Impuestos calculados correctamente.")
    @PostMapping("/pay-isr")
    public ResponseEntity<String> payISR(@RequestParam Long userId) {
        try {
            taxService.paySpecificTax(userId, TaxCategory.ISR);
            return ResponseEntity.ok("Impuesto ISR pagado exitosamente.");
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Operation(
            summary = "PRIORIDAD, asocia el monto total de los ingresos al flujo de mis impuestos",
            description = "Asocia el monto total de Ingresos a mis Pagos de Impuestos"
    )
    @ApiResponse(responseCode = "200", description = "Impuestos asociados correctamente.")
    @PostMapping("/initialize-balance/{userId}")
    public ResponseEntity<String> initializeIncomeBalance(@PathVariable Long userId) {
        taxService.initializeIncomeBalance(userId);
        return ResponseEntity.ok("El saldo de ingresos se inicializó exitosamente.");
    }

    @Operation(
            summary = "Obtiene el total de pago de los impuestos IVA + ISR",
            description = "Muestra el monto total de pagos de impuestos"
    )
    @ApiResponse(responseCode = "200", description = "Total de Impuestos calculados correctamente.")
    @GetMapping("/total-payments")
    public ResponseEntity<String> getTotalPayments(@RequestParam Long userId) {
        double totalPayments = taxService.calculateTotalPayments(userId);
        return ResponseEntity.ok("Pagos totales realizados: " + totalPayments);
    }

    @Operation(
            summary = "Obtiene el total de deuda de los impuestos IVA + ISR",
            description = "Muestra el monto total de deuda de impuestos"
    )
    @ApiResponse(responseCode = "200", description = "Total de deuda de Impuestos calculados correctamente.")
    @GetMapping("/total-debts")
    public ResponseEntity<String> getTotalDebts(@RequestParam Long userId) {
        double totalDebts = taxService.calculateTotalDebts(userId);
        return ResponseEntity.ok("Pagos totales realizados: " + totalDebts);
    }
}
