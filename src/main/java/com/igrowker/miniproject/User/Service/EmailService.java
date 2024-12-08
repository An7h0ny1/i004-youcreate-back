package com.igrowker.miniproject.User.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.igrowker.miniproject.User.Model.UserEntity;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.host}")
    private String host;

    public UUID sendResetPassword(UserEntity user, String email){

        try {
            UUID token = generateResetPasswordToken();
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(email);
            message.setSubject("Email Verification Code for password reset");
            message.setText("Your reset token code is: " + token.toString());
            mailSender.send(message);
            return token;
        } catch (Exception e) {
            throw e;
        }
    }

    public String sendEmailForVerification2FA(String email){
        try {
            Integer token = generateRandom2FAToken();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Email Verification Code for password reset");
            message.setText("Your register verification code is: " + token.toString());
            mailSender.send(message);
            return token.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private Integer generateRandom2FAToken(){
        return new Random().nextInt(9000) + 1000;
    }

    private UUID generateResetPasswordToken(){
        return UUID.randomUUID();
    }

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}