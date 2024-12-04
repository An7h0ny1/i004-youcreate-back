package com.igrowker.miniproject.TaxObligation.Controller;
import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Service.TaxNotificationService;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .map(notification -> new TaxDTO(
                        notification.getUser().getId(),
                        notification.getUser().getUserName(),
                        notification.getCountry(),
                        notification.getTaxDeadline(),
                        notification.getLastNotifiedDate(),
                        notification.isPaymentConfirmed()))
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
}
