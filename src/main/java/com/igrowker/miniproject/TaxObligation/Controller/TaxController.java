package com.igrowker.miniproject.TaxObligation.Controller;
import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/taxes")
public class TaxController {


    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
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
                        notification.isNotified()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(notificationDTOs);
    }
}
