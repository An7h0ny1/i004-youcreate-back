package com.igrowker.miniproject.User.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.igrowker.miniproject.User.Dto.TwoFARegisterVerificationDTO;
import com.igrowker.miniproject.User.Model.RegisterVerification2FA;
import com.igrowker.miniproject.User.Repository.RegisterVerification2FARepository;

@Service
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

    public String verificate2FAtoken(TwoFARegisterVerificationDTO fa) throws Exception {

        if (fa.email() == null) 
            throw new BadRequestException("No se paso el email por parametro");
        
        if (fa.token() == null) {
            throw new BadRequestException("No se paso el token por parametro");
        }
        Optional<RegisterVerification2FA> register = registerVerification2FARepository.findByEmail(fa.email());
        if (register.isPresent()) {
            
            String token  = register.get().getToken();
            if (register.get().getStatus() != null) {
                return "USED";
            }
            if (!token.equals(fa.token())) return "INVALID";
            if (tokenIsExpired(register.get())) return "EXPIRED";
            RegisterVerification2FA register_bdd = register.get();
            register_bdd.setStatus("USED");
            registerVerification2FARepository.save(register_bdd);
            return "OK";
        }
        throw new NoSuchElementException("No se encontro al registro de ese usuario");
    }

    private boolean tokenIsExpired(RegisterVerification2FA registerVerification2FA){
        return registerVerification2FA.getExpired_at().isBefore(LocalDateTime.now());
    }
}
