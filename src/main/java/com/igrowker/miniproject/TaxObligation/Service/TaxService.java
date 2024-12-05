package com.igrowker.miniproject.TaxObligation.Service;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import com.igrowker.miniproject.TaxObligation.Repository.TaxTypeRepository;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import com.igrowker.miniproject.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TaxService {

    private final TaxNotificationService taxNotificationService;
    private final TaxNotificationEmailService taxNotificationEmailService;
    private final TaxNotificationRepository taxNotificationRepository;
    private final TaxTypeRepository taxTypeRepository;
    private final UserService userService;

    /*
    // Mapa estático de Fecha Limite de pagos
    protected static final Map<String, LocalDate> TAX_DEADLINES = Map.of(
            "Bolivia", LocalDate.of(2024, 12, 02),
            "Argentina", LocalDate.of(2024, 12, 02),
            "Colombia", LocalDate.of(2024, 12, 02),
            "Dominican Republic", LocalDate.of(2024, 11, 02),
            "Costa Rica", LocalDate.of(2024, 12, 02),
            "Spain", LocalDate.of(2024, 12, 02)
    );

     */

    @Autowired
    public TaxService(TaxNotificationService taxNotificationService, TaxNotificationEmailService taxNotificationEmailService, TaxNotificationRepository taxNotificationRepository, TaxTypeRepository taxTypeRepository, UserService userService) {
        this.taxNotificationService = taxNotificationService;
        this.taxNotificationEmailService = taxNotificationEmailService;
        this.taxNotificationRepository = taxNotificationRepository;
        this.taxTypeRepository = taxTypeRepository;
        this.userService = userService;
    }

    /*
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

     */
    // Tarea programada para enviar notificaciones de próximos plazos
    @Scheduled(cron = "0 0 9 * * ?") // Runs daily at 9 a.m.
    public void checkAndSendNotifications() {
        // Find notifications whose deadlines fall within the next 4 days
        List<TaxNotificationEntity> notifications =
                taxNotificationRepository.findByTaxDeadlineBetween(LocalDate.now(), LocalDate.now().plusDays(4));

        for (TaxNotificationEntity notification : notifications) {
            // Ensure the notification hasn't already been sent today
            if (!LocalDate.now().equals(notification.getLastNotifiedDate())) {
                // Fetch details from the TaxType associated with the notification
                TaxType taxType = notification.getTaxType();

                String email = notification.getUser().getEmail();
                String country = taxType.getCountry(); // Get country from TaxType
                LocalDate deadline = taxType.getExpirationDate(); // Get deadline from TaxType

                // Send the email notification
                taxNotificationEmailService.sendTaxDeadlineNotification(email, country, deadline);

                // Update the last notified date and save the notification
                notification.setLastNotifiedDate(LocalDate.now());
                taxNotificationRepository.save(notification);
            }
        }
    }

    // Obtener notificaciones para un usuario específico
    public List<TaxNotificationEntity> getNotificationsForUser(Long userId) {
        return taxNotificationService.getNotificationsForUser(userId);
    }

    public BigDecimal calculateTaxForUser(Long userId, String taxName) {
        UserEntity user = userService.getUserById(userId);

        // Use TaxType to fetch tax details
        TaxType taxType = taxTypeRepository.findByTaxName(taxName)
                .orElseThrow(() -> new IllegalArgumentException("Tax type not found for tax name: " + taxName));

        BigDecimal baseAmount = taxType.getBaseAmount();
        BigDecimal percentage = taxType.getPercentage();
        return baseAmount.multiply(percentage.divide(BigDecimal.valueOf(100)));
    }
}
