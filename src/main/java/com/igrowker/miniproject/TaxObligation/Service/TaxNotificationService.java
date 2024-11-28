package com.igrowker.miniproject.TaxObligation.Service;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import com.igrowker.miniproject.User.Model.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        TaxNotificationEntity taxNotification = new TaxNotificationEntity(user, country, taxDeadline);
        taxNotificationRepository.save(taxNotification);

        // Enviar notificación por correo electrónico si la fecha límite es hoy
        if (taxDeadline.equals(LocalDate.now())) {
            taxNotificationEmailService.sendTaxDeadlineNotification(user.getEmail(), country, taxDeadline);
            taxNotification.setNotified(true);
            taxNotificationRepository.save(taxNotification);
        }
    }


    // Recibir notificaciones para un usuario
    public List<TaxNotificationEntity> getNotificationsForUser(Long userId) {
        return taxNotificationRepository.findByUser_Id(userId);
    }

    // Obtener las próximas notificaciones
    public List<TaxNotificationEntity> findUpcomingNotifications() {
        return taxNotificationRepository.findByIsNotifiedFalseAndTaxDeadlineBefore(LocalDate.now().plusDays(1));
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
        List<TaxNotificationEntity> notifications = taxNotificationRepository.findByIsNotifiedFalseAndTaxDeadlineBefore(LocalDate.now().plusDays(7));

        notifications.forEach(notification -> {
            try {
                String email = notification.getUser().getEmail();
                String country = notification.getCountry();
                LocalDate deadline = notification.getTaxDeadline();

                // Enviar correo electrónico de notificación
                taxNotificationEmailService.sendTaxDeadlineNotification(email, country, deadline);

                // Marcar como notificado
                notification.setNotified(true);
                taxNotificationRepository.save(notification);

            } catch (Exception e) {
                System.err.println("Failed to notify user: " + e.getMessage());
            }
        });
    }
}
