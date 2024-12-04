package com.igrowker.miniproject.TaxObligation.Service;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import com.igrowker.miniproject.User.Model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.igrowker.miniproject.TaxObligation.Service.TaxService.TAX_DEADLINES;

@Service
public class TaxNotificationService {

    private final TaxNotificationRepository taxNotificationRepository;
    private final TaxNotificationEmailService taxNotificationEmailService;


    @Autowired
    public TaxNotificationService(TaxNotificationRepository taxNotificationRepository, TaxNotificationEmailService taxNotificationEmailService) {
        this.taxNotificationRepository = taxNotificationRepository;
        this.taxNotificationEmailService = taxNotificationEmailService;
    }

    @Transactional
    public void createOrUpdateTaxNotification(UserEntity user) {
        String country = user.getCountry();
        LocalDate taxDeadline = getTaxDeadlineForCountry(country);

        // Crear o actualizar la entidad de notificación
        TaxNotificationEntity taxNotification = taxNotificationRepository.findByUserAndCountry(user, country)
                .orElse(new TaxNotificationEntity(user, country, taxDeadline));
        taxNotification.setTaxDeadline(taxDeadline);
        taxNotificationRepository.save(taxNotification);

        // Saltar notificación si se confirma el pago
        if (taxNotification.isPaymentConfirmed()) {
            return;
        }

        // Enviar notificación por correo electrónico si está dentro del rango de notificación
        if (!LocalDate.now().isAfter(taxDeadline) &&
                !LocalDate.now().isBefore(taxDeadline.minusDays(4))) {

            if (taxNotification.getLastNotifiedDate() == null || !taxNotification.getLastNotifiedDate().equals(LocalDate.now())) {
                taxNotificationEmailService.sendTaxDeadlineNotification(user.getEmail(), country, taxDeadline);

                // Actualizar la última fecha notificada
                taxNotification.setLastNotifiedDate(LocalDate.now());
                taxNotificationRepository.save(taxNotification);
            }
        }
    }


    // Recibir notificaciones para un usuario
    public List<TaxNotificationEntity> getNotificationsForUser(Long userId) {
        return taxNotificationRepository.findByUser_Id(userId);
    }

    // Obtener las próximas notificaciones
    public List<TaxNotificationEntity> findUpcomingNotifications() {
        return taxNotificationRepository.findByIsNotifiedFalseAndTaxDeadlineBefore(LocalDate.now().plusDays(4));
    }

    // Guardar notificación
    @Transactional
    public void saveNotification(TaxNotificationEntity notification) {
        taxNotificationRepository.save(notification);
    }

    private LocalDate getTaxDeadlineForCountry(String country) {
        LocalDate deadline = TAX_DEADLINES.get(country);
        if (deadline == null) {
            throw new IllegalArgumentException("No tax deadline found for country: " + country);
        }
        return deadline;
    }

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
