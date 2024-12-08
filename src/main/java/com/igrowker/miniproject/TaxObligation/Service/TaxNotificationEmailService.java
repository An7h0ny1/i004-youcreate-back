package com.igrowker.miniproject.TaxObligation.Service;

import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TaxNotificationEmailService {

    private final JavaMailSender mailSender;

    public TaxNotificationEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTaxDeadlineNotification(String email, String country, LocalDate deadline) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Próxima fecha límite de impuestos");
            message.setText("Hola Estimado Usuario,\n\nTenga en cuenta que la fecha límite de impuestos para "
                    + country + " se acerca " + deadline + ".\n\nQue tenga Buen Dia!");
            mailSender.send(message);
            System.out.println("Correo electrónico de notificación de impuestos enviado a " + email);
        } catch (Exception e) {
            System.err.println("No se pudo enviar el correo electrónico de notificación de impuestos: " + e.getMessage());
        }
    }


    public void sendDailyPaymentReminder(String email, TaxType tax) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Recordatorio de pago de impuestos pendientes");
            message.setText("Estimado usuari@,\n\nTiene un pago de impuestos pendiente por " + tax.getCategory() +
                    ". La fecha límite es " + tax.getExpirationDate() +
                    ".\n\nPor favor realice el pago lo antes posible.\n\nAtentamente,\nEquipo de Sistema Tributario");
            mailSender.send(message);
            System.out.println("Recordatorio de pago diario enviado a " + email);
        } catch (Exception e) {
            System.err.println("No se pudo enviar el correo electrónico de recordatorio de pago: " + e.getMessage());
        }
    }

}
