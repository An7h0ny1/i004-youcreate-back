package com.igrowker.miniproject.User.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.igrowker.miniproject.Config.Jwt.JwtUtils;
import com.igrowker.miniproject.User.Model.PasswordReset;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.PasswordResetRepository;
import com.igrowker.miniproject.User.Repository.UserRepository;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetRepository resetrepository;

    @Autowired
    private EmailService mailSender;

    public void sendEmailReset(String email) throws Exception {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            UUID token = mailSender.sendResetPassword(user.get(), email);
            LocalDateTime datetime = LocalDateTime.now();
            PasswordReset reset = new PasswordReset();

            reset.setEmail(email);
            reset.setToken(token);
            reset.setCreated_at(datetime);
            reset.setExpired_at(datetime.plusMinutes(5));
            resetrepository.save(reset);

        } else {
            throw new RuntimeException("No se ha encontrado a un usuario con ese mail");
        }

    }

    private boolean isValidToken(String token, PasswordReset passwordreset) {

        if (passwordreset.getExpired_at().isBefore(LocalDateTime.now())) { // si fecha de expiracion < momento actual
            return false;
        }
        return true;
    }

    public boolean validate(String token, String email, String password) throws Exception {

        Optional<PasswordReset> passwordreset = resetrepository.findByEmail(email);
        System.out.println("el email es " + email);
        if (passwordreset.isPresent()) {
            if (!isValidToken(token, passwordreset.get()))
                return false;

            UserEntity user = userRepository.findByEmail(email).get();
            user.setPassword(password);
            userRepository.save(user);
            
            return true;
        }
        return false;

    }

    public java.util.List<PasswordReset> getAllPasswordsReset() {
        return resetrepository.findAll();
    }

}
