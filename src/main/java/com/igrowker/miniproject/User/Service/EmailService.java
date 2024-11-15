package com.igrowker.miniproject.User.Service;

import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.igrowker.miniproject.User.Model.UserEntity;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    private final String host = "http//localhost:8080/reset/";

    public UUID sendResetPassword(UserEntity user, String email){

        try {
            UUID token = UUID.randomUUID();
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(email);
            message.setSubject("Email Verification Code for password reset");
            message.setText("Your verification code is: "+ host + token.toString());
            mailSender.send(message);
            return token;
        } catch (Exception e) {
            throw e;
        }
    }

    public String sendEmailForVerification2FA(String email){
        try {
            Integer token = new Random().nextInt(9000) + 1000;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Email Verification Code for password reset");
            message.setText("Your register verification code is: "+ host + token.toString());
            mailSender.send(message);
            return token.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    
}