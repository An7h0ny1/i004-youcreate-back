package com.igrowker.miniproject.TaxObligation.Controller;
import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.TaxObligation.Service.TaxNotificationService;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/taxes")
public class TaxController {


    private final TaxService taxService;
    private final TaxNotificationService taxNotificationService;

    public TaxController(TaxService taxService, TaxNotificationService taxNotificationService) {
        this.taxService = taxService;
        this.taxNotificationService = taxNotificationService;
    }

    // Endpoint para recibir notificaciones de impuestos para un usuario
    @GetMapping("/notifications/{userId}")
    public ResponseEntity<List<TaxDTO>> getTaxNotificationsForUser(@PathVariable Long userId) {
        List<TaxNotificationEntity> notifications = taxService.getNotificationsForUser(userId);

        List<TaxDTO> notificationDTOs = notifications.stream()
                .map(notification -> {
                    TaxType taxType = notification.getTaxType(); // Fetch the associated TaxType
                    return new TaxDTO(
                            notification.getUser().getId(),
                            notification.getUser().getUserName(),
                            taxType.getCountry(),         // From TaxType
                            taxType.getTaxName(),         // Added Tax Name
                            taxType.getExpirationDate(),  // From TaxType
                            notification.getLastNotifiedDate(),
                            notification.isPaymentConfirmed()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOs);
    }


    @PutMapping("/{id}/confirm")
    public ResponseEntity<String> confirmPayment(@PathVariable Long id) {
        boolean success = taxNotificationService.confirmPayment(id);
        if (success) {
            return ResponseEntity.ok("Payment confirmed successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found.");
        }
    }

    // Calculate tax amount for a user
    @GetMapping("/{userId}/calculate/{taxName}")
    public ResponseEntity<BigDecimal> calculateTaxForUser(@PathVariable Long userId, @PathVariable String taxName) {
        BigDecimal taxAmount = taxService.calculateTaxForUser(userId, taxName);
        return ResponseEntity.ok(taxAmount);
    }
}
