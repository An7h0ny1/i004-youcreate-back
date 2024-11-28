package com.igrowker.miniproject.TaxObligation.Service;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TaxService {

    private final TaxNotificationService taxNotificationService;
    private final TaxNotificationEmailService taxNotificationEmailService;
    private final TaxNotificationRepository taxNotificationRepository;

    // Mapa estático de Fecha Limite de pagos
    protected static final Map<String, LocalDate> TAX_DEADLINES = Map.of(
            "Bolivia", LocalDate.of(2024, 11, 30),
            "Argentina", LocalDate.of(2024, 12, 28),
            "Colombia", LocalDate.of(2024, 12, 28),
            "Dominican Republic", LocalDate.of(2024, 12, 28),
            "Costa Rica", LocalDate.of(2024, 12, 28),
            "Spain", LocalDate.of(2024, 12, 28)
    );

    @Autowired
    public TaxService(TaxNotificationService taxNotificationService, TaxNotificationEmailService taxNotificationEmailService, TaxNotificationRepository taxNotificationRepository) {
        this.taxNotificationService = taxNotificationService;
        this.taxNotificationEmailService = taxNotificationEmailService;
        this.taxNotificationRepository = taxNotificationRepository;
    }

    // Tarea programada para enviar notificaciones de próximos plazos
    @Scheduled(cron = "0 0 9 * * ?") // Funciona todos los días a las 9 a.m.
    public void checkAndSendNotifications() {
        List<TaxNotificationEntity> upcomingNotifications =
                taxNotificationService.findUpcomingNotifications();

        for (TaxNotificationEntity notification : upcomingNotifications) {
            String email = notification.getUser().getEmail();
            String country = notification.getCountry();
            LocalDate deadline = notification.getTaxDeadline();

            taxNotificationEmailService.sendTaxDeadlineNotification(email, country, deadline);

            // Marcar como notificado y guardar
            notification.setNotified(true);
            taxNotificationService.saveNotification(notification);
        }
    }

    // Obtener notificaciones para un usuario específico
    public List<TaxNotificationEntity> getNotificationsForUser(Long userId) {
        return taxNotificationService.getNotificationsForUser(userId);
    }

}
