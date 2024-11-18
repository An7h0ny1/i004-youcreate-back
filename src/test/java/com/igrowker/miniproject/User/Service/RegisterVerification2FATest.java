package com.igrowker.miniproject.User.Service;

import com.igrowker.miniproject.User.Dto.TwoFARegisterVerificationDTO;
import com.igrowker.miniproject.User.Model.RegisterVerification2FA;
import com.igrowker.miniproject.User.Repository.RegisterVerification2FARepository;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


//pruebas hechas para h2, no correr en produccion
@SpringBootTest
public class RegisterVerification2FATest{

  
    
    @Autowired
    private RegisterVerification2FARepository repository;

   
    @Autowired
    private IRegisterVerification2FAService service;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Enviar un mail correctamente")
    public void sendEmailForVerification2FA() throws Exception{

        RegisterVerification2FA register = new RegisterVerification2FA();

        register.setEmail("robotit@mail.com");

      
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
    public void TokenExpiredHappyTest() throws Exception{
        
        RegisterVerification2FA register = new RegisterVerification2FA();

        register.setEmail("gabriellimiguel@mail.com");
        service.sendEmailForVerification2FA(register.getEmail());

        Optional<RegisterVerification2FA> result = repository.findByEmail(register.getEmail());

        assertNotNull(result.get().getExpired_at());
        assertTrue(LocalDateTime.now().isBefore(result.get().getExpired_at()));
    }

    @Test
    @DisplayName("Deberia lanzar un no such element exception al no esta el email en la base de datos")
    public void verificate2FAtokenMailNotFoundTest(){
        TwoFARegisterVerificationDTO fa = new TwoFARegisterVerificationDTO("christian@gmail.com", "1234");
        
        assertThrows(NoSuchElementException.class, () -> service.verificate2FAtoken(fa));

    }

    @Test
    @DisplayName("Deberia lanzar un bad request exception si el email es null")
    public void verificate2FAtokenMailIsNullTest(){
        TwoFARegisterVerificationDTO fa = new TwoFARegisterVerificationDTO(null, "1234");
        
        assertThrows(BadRequestException.class, () -> service.verificate2FAtoken(fa));

    }

    @Test
    @DisplayName("Deberia lanzar un bad request exception si el token es null")
    public void verificate2FAtokenTokenIsNullTest(){
        TwoFARegisterVerificationDTO fa = new TwoFARegisterVerificationDTO("christian@mail.com", null);
        
        assertThrows(BadRequestException.class, () -> service.verificate2FAtoken(fa));

    }


    @Test
    @DisplayName("Se analiza un token ya utilizada")
    public void verificate2FAtokenAndTokenIsUsed() throws Exception{
        TwoFARegisterVerificationDTO fa = new TwoFARegisterVerificationDTO("christian@mail.com", "1234");
        RegisterVerification2FA register = new RegisterVerification2FA();
        register.setEmail("christian@mail.com");
        register.setToken("1234");
        register.setCreated_at(LocalDateTime.now());
        register.setExpired_at(LocalDateTime.now().plusMinutes(3));

        assertDoesNotThrow(() -> repository.save(register));

        assertEquals(service.verificate2FAtoken(fa), "OK");
        assertEquals(repository.findByEmail(fa.email()).get().getStatus(), "USED");


    }

    @Test
    @DisplayName("Se analiza un token que es distinto al pasado por body")
    public void verificate2FAtokenTokenButTokenIsNotEquals() throws Exception {
        TwoFARegisterVerificationDTO fa = new TwoFARegisterVerificationDTO("christian@mail.com", "3555");
        RegisterVerification2FA register = new RegisterVerification2FA();
        register.setEmail("christian@mail.com");
        register.setToken("1234");
        register.setCreated_at(LocalDateTime.now());
        register.setExpired_at(LocalDateTime.now().plusMinutes(3));

        assertDoesNotThrow(() -> repository.save(register));
        assertEquals(service.verificate2FAtoken(fa), "INVALID");


    }

    @Test
    @DisplayName("Se analiza un token que ya esta expirado")
    public void verificate2FAtokenTokenButTokenIsExpired() throws Exception {
        TwoFARegisterVerificationDTO fa = new TwoFARegisterVerificationDTO("christian@mail.com", "1234");
        RegisterVerification2FA register = new RegisterVerification2FA();
        register.setEmail("christian@mail.com");
        register.setToken("1234");
        register.setCreated_at(LocalDateTime.now());
        register.setExpired_at(LocalDateTime.now().minusMinutes(3));

        assertDoesNotThrow(() -> repository.save(register));
        assertEquals(service.verificate2FAtoken(fa), "EXPIRED");


    }

}