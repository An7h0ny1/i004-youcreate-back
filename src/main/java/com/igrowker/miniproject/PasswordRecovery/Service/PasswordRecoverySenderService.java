package com.igrowker.miniproject.PasswordRecovery.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;

@Service
public class PasswordRecoverySenderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailVerification(String email) throws Exception {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Email Verification Code for password reset");
            message.setText("Your verification code is: ");
            mailSender.send(message);
        } else {
            throw new RuntimeException("No se ha encontrado a un usuario con ese mail");
        }

    }

}
