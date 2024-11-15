package com.igrowker.miniproject.User.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import com.igrowker.miniproject.User.Dto.TwoFARegisterVerificationDTO;
import com.igrowker.miniproject.User.Model.RegisterVerification2FA;
import com.igrowker.miniproject.User.Repository.RegisterVerification2FARepository;
import com.igrowker.miniproject.User.Repository.UserRepository;

public class RegisterVerification2FAService {

    @Autowired
    private RegisterVerification2FARepository registerVerification2FARepository;

    @Autowired
    private EmailService mailSender;

    public List<RegisterVerification2FA> getAllRegisters() {
        return registerVerification2FARepository.findAll();
    }

    public void sendEmailForVerification2FA(String email) throws Exception {

        try {
            String token = mailSender.sendEmailForVerification2FA(email);
            LocalDateTime datetime = LocalDateTime.now();
            RegisterVerification2FA fa = new RegisterVerification2FA();

            fa.setEmail(email);
            fa.setToken(token);
            fa.setCreated_at(datetime);
            fa.setExpired_at(datetime.plusMinutes(3));
            registerVerification2FARepository.save(fa);
        } catch (Exception e) {
            throw e;
        }

    }

    public Optional<RegisterVerification2FA> validateEmail(String email) {
        return registerVerification2FARepository.findByEmail(email);
    }

    public boolean verificate2FAtoken(TwoFARegisterVerificationDTO fa) throws Exception {
        Optional<RegisterVerification2FA> register = registerVerification2FARepository.findByEmail(fa.email());
        if (register.isPresent()) {
            
            String token  = register.get().getToken();
            return token.equals(fa.email());
        }
        else return false;
    }
}
