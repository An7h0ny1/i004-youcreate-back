package com.igrowker.miniproject.TaxObligation.Service;
import com.igrowker.miniproject.TaxObligation.Dto.TaxCategory;
import com.igrowker.miniproject.TaxObligation.Dto.TaxStatus;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import com.igrowker.miniproject.TaxObligation.Repository.TaxTypeRepository;
import com.igrowker.miniproject.User.Model.User;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Service.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TaxNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(TaxNotificationService.class);

    private final TaxNotificationRepository taxNotificationRepository;
    private final TaxNotificationEmailService taxNotificationEmailService;
    private final TaxTypeRepository taxTypeRepository;


    @Autowired
    public TaxNotificationService(TaxNotificationRepository taxNotificationRepository, TaxNotificationEmailService taxNotificationEmailService, TaxTypeRepository taxTypeRepository) {
        this.taxNotificationRepository = taxNotificationRepository;
        this.taxNotificationEmailService = taxNotificationEmailService;
        this.taxTypeRepository = taxTypeRepository;
    }

    /*

    public void associateTaxesForUserBasedOnCountry(UserEntity user) {
        // Tax configurations mapped by country
        Map<String, List<TaxType>> taxConfig = new HashMap<>();

        // Example tax configurations for different countries
        taxConfig.put("Bolivia", List.of(
                new TaxType(TaxCategory.VAT, 13.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user),
                new TaxType(TaxCategory.ISR, 15.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user)
        ));

        taxConfig.put("Argentina", List.of(
                new TaxType(TaxCategory.VAT, 27.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user),
                new TaxType(TaxCategory.ISR, 29.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user)
        ));

        taxConfig.put("Colombia", List.of(
                new TaxType(TaxCategory.VAT, 19.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user),
                new TaxType(TaxCategory.ISR, 25.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user)
        ));

        taxConfig.put("Dominican Republic", List.of(
                new TaxType(TaxCategory.VAT, 18.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user),
                new TaxType(TaxCategory.ISR, 24.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user)
        ));

        taxConfig.put("Costa Rica", List.of(
                new TaxType(TaxCategory.VAT, 13.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user),
                new TaxType(TaxCategory.ISR, 18.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user)
        ));

        taxConfig.put("Spain", List.of(
                new TaxType(TaxCategory.VAT, 21.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user),
                new TaxType(TaxCategory.ISR, 25.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, user)
        ));

        // Fetch tax configuration for the user’s country
        List<TaxType> userTaxes = taxConfig.get(user.getCountry());

        if (userTaxes != null) {
            // Save all tax types for the user
            taxTypeRepository.saveAll(userTaxes);
        } else {
            // Log a warning or throw an exception if country is not found
            String errorMessage = "No tax configuration found for the country: " + user.getCountry();
            // Logging the error
            logger.warn(errorMessage);  // Make sure you have a logger in place
            throw new IllegalArgumentException(errorMessage);  // Optionally throw an exception
        }
    }


    // Updated method to create or update notifications
    @Transactional
    public void createOrUpdateTaxNotification(UserEntity user) {
        // Fetch all tax types associated with the user's country
        List<TaxType> taxTypes = taxTypeRepository.findByUserCountry(user.getCountry()); // Updated to filter by user's country
        if (taxTypes.isEmpty()) {
            throw new IllegalArgumentException("No tax types found for the user's country.");
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
                    // Fetch country from UserEntity, no longer from TaxType
                    String country = user.getCountry(); // Use the user's country directly

                    // Send the notification email
                    taxNotificationEmailService.sendTaxDeadlineNotification(
                            user.getEmail(),
                            country, // Use the country from the UserEntity
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
        List<TaxType> taxTypes = taxTypeRepository.findByUserCountry(country);
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
                    // Fetch country from UserEntity (instead of TaxType)
                    String country = notification.getUser().getCountry(); // Use the country from UserEntity

                    // Send the notification email
                    taxNotificationEmailService.sendTaxDeadlineNotification(
                            notification.getUser().getEmail(),
                            country, // Use the country from the UserEntity
                            taxType.getExpirationDate() // Get expiration date from TaxType
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

     */
}
