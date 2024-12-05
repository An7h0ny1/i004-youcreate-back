package com.igrowker.miniproject.TaxObligation.Service;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import com.igrowker.miniproject.TaxObligation.Repository.TaxTypeRepository;
import com.igrowker.miniproject.User.Model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TaxNotificationService {

    private final TaxNotificationRepository taxNotificationRepository;
    private final TaxNotificationEmailService taxNotificationEmailService;
    private final TaxTypeRepository taxTypeRepository;


    @Autowired
    public TaxNotificationService(TaxNotificationRepository taxNotificationRepository, TaxNotificationEmailService taxNotificationEmailService, TaxTypeRepository taxTypeRepository) {
        this.taxNotificationRepository = taxNotificationRepository;
        this.taxNotificationEmailService = taxNotificationEmailService;
        this.taxTypeRepository = taxTypeRepository;
    }

    // Updated method to create or update notifications
    @Transactional
    public void createOrUpdateTaxNotification(UserEntity user) {
        // Fetch all tax types
        List<TaxType> taxTypes = taxTypeRepository.findAll();
        if (taxTypes.isEmpty()) {
            throw new IllegalArgumentException("No tax types found in the system.");
        }

        for (TaxType taxType : taxTypes) {
            // Check if a notification already exists for the user and specific tax type
            TaxNotificationEntity taxNotification = taxNotificationRepository
                    .findByUserAndTaxType(user, taxType)
                    .orElse(new TaxNotificationEntity(user, taxType)); // Updated constructor usage

            // Update the tax deadline
            taxNotification.setTaxDeadline(taxType.getExpirationDate());
            taxNotificationRepository.save(taxNotification);

            // Skip notification if payment is confirmed
            if (taxNotification.isPaymentConfirmed()) {
                continue;
            }

            // Notify if within the range
            if (!LocalDate.now().isAfter(taxType.getExpirationDate()) &&
                    !LocalDate.now().isBefore(taxType.getExpirationDate().minusDays(4))) {
                if (taxNotification.getLastNotifiedDate() == null ||
                        !taxNotification.getLastNotifiedDate().equals(LocalDate.now())) {
                    taxNotificationEmailService.sendTaxDeadlineNotification(
                            user.getEmail(),
                            taxType.getCountry(), // Use TaxType to get the country
                            taxType.getExpirationDate()
                    );

                    taxNotification.setLastNotifiedDate(LocalDate.now());
                    taxNotificationRepository.save(taxNotification);
                }
            }
        }
    }


    // Recibir notificaciones para un usuario
    public List<TaxNotificationEntity> getNotificationsForUser(Long userId) {
        return taxNotificationRepository.findByUser_Id(userId);
    }

    // Obtener las próximas notificaciones
    public List<TaxNotificationEntity> findUpcomingNotifications() {
        // Get tax types with expiration dates in the next 4 days
        List<TaxType> upcomingTaxTypes = taxTypeRepository.findByExpirationDateBefore(LocalDate.now().plusDays(4));

        // Retrieve notifications for those deadlines
        return taxNotificationRepository.findByIsNotifiedFalseAndTaxDeadlineIn(
                upcomingTaxTypes.stream().map(TaxType::getExpirationDate).toList()
        );
    }

    // Guardar notificación
    @Transactional
    public void saveNotification(TaxNotificationEntity notification) {
        taxNotificationRepository.save(notification);
    }

    private LocalDate getTaxDeadlineForCountry(String country) {
        List<TaxType> taxTypes = taxTypeRepository.findByCountry(country);
        if (taxTypes.isEmpty()) {
            throw new IllegalArgumentException("No tax deadlines found for country: " + country);
        }

        // Assume one tax type per country or return the earliest deadline
        return taxTypes.stream()
                .map(TaxType::getExpirationDate)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new IllegalStateException("Unexpected error while fetching tax deadlines."));
    }

    /*
    // Método para notificar a los usuarios sobre los próximos plazos
    @Transactional
    public void notifyUsersAboutUpcomingDeadlines() {
        List<TaxNotificationEntity> notifications = taxNotificationRepository.findByTaxDeadlineBetween(
                LocalDate.now(),
                LocalDate.now().plusDays(4)
        );

        notifications.forEach(notification -> {
            if (notification.isPaymentConfirmed()) {
                return; // Saltar si se confirma el pago
            }

            try {
                String email = notification.getUser().getEmail();
                String country = notification.getCountry();
                LocalDate deadline = notification.getTaxDeadline();

                // Compruebe si ya se envió una notificación hoy
                if (notification.getLastNotifiedDate() == null || !notification.getLastNotifiedDate().equals(LocalDate.now())) {
                    // Send email notification
                    taxNotificationEmailService.sendTaxDeadlineNotification(email, country, deadline);

                    // Actualizar el campo lastNotifiedDate
                    notification.setLastNotifiedDate(LocalDate.now());
                    taxNotificationRepository.save(notification);
                }
            } catch (Exception e) {
                System.err.println("Failed to notify user: " + e.getMessage());
            }
        });
    }

     */

    @Scheduled(cron = "0 0 9 * * ?") // Runs daily at 9 a.m.
    public void notifyUsersAboutUpcomingDeadlines() {
        // Fetch all tax types with expiration dates within the next 4 days
        List<TaxType> upcomingTaxes = taxTypeRepository.findByExpirationDateBefore(LocalDate.now().plusDays(4));

        for (TaxType taxType : upcomingTaxes) {
            // Find notifications for the specific tax type
            List<TaxNotificationEntity> notifications = taxNotificationRepository.findByTaxTypeAndTaxDeadlineBetween(
                    taxType, LocalDate.now(), taxType.getExpirationDate()
            );

            notifications.forEach(notification -> {
                if (!notification.isPaymentConfirmed() &&
                        (notification.getLastNotifiedDate() == null ||
                                !notification.getLastNotifiedDate().equals(LocalDate.now()))) {
                    taxNotificationEmailService.sendTaxDeadlineNotification(
                            notification.getUser().getEmail(),
                            taxType.getCountry(), // Updated to fetch country from TaxType
                            taxType.getExpirationDate() // Updated to fetch expiration date from TaxType
                    );

                    // Update the last notified date and save the notification
                    notification.setLastNotifiedDate(LocalDate.now());
                    taxNotificationRepository.save(notification);
                }
            });
        }
    }

    @Transactional
    public boolean confirmPayment(Long notificationId) {
        Optional<TaxNotificationEntity> notificationOpt = taxNotificationRepository.findById(notificationId);

        if (notificationOpt.isPresent()) {
            TaxNotificationEntity notification = notificationOpt.get();
            notification.setPaymentConfirmed(true);
            taxNotificationRepository.save(notification);
            return true;
        }
        return false;
    }
}
