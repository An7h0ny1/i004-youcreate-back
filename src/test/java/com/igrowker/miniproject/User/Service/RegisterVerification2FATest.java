package com.igrowker.miniproject.User.Service;

import com.igrowker.miniproject.User.Model.RegisterVerification2FA;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.RegisterVerification2FARepository;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RegisterVerification2FATest{

  
    @Autowired
    private RegisterVerification2FARepository repository;
    @Autowired
    private RegisterVerification2FAService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Enviar un mail correctamente")
    public void sendEmailForVerification2FA() throws Exception{

        RegisterVerification2FA register = new RegisterVerification2FA();

        register.setEmail("gabriellimiguel123@mail.com");

      
        assertDoesNotThrow(() -> {
            service.sendEmailForVerification2FA(register.getEmail());
        });

        Optional<RegisterVerification2FA> result = repository.findByEmail(register.getEmail());
        
        assertNotNull(result.get());
        assertEquals(result.get().getEmail(), register.getEmail());
        assertNotNull(result.get().getExpired_at());
    }

    @Test
    @DisplayName("El email no es valido, por lo que se lanza una excepcion")
    public void FailedEmailCase(){

        RegisterVerification2FA register = new RegisterVerification2FA();

        register.setEmail("pepe");

        assertThrows(Exception.class, () -> {
            service.sendEmailForVerification2FA(register.getEmail());
        });

    }

    @Test
    @DisplayName("El token de expiracion esta antes del tiempo actual")
    public void TokenExpiredTest() throws Exception{
        
        RegisterVerification2FA register = new RegisterVerification2FA();

        register.setEmail("gabriellimiguel@mail.com");
        service.sendEmailForVerification2FA(register.getEmail());

        Optional<RegisterVerification2FA> result = repository.findByEmail(register.getEmail());

        assertNotNull(result.get().getExpired_at());
        assertTrue(LocalDateTime.now().isBefore(result.get().getExpired_at()));
    }
}