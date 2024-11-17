package com.igrowker.miniproject.User.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.igrowker.miniproject.User.Model.PasswordReset;
import com.igrowker.miniproject.User.Repository.PasswordResetRepository;

@SpringBootTest
public class PasswordResetTest {
 
    @Mock
    @Autowired
    PasswordResetRepository repository;
    @InjectMocks
    @Autowired
    PasswordResetService service;

    private String email;
    private UUID uuid;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void saveAResetToken(){
        Mockito.when(repository.save(Mockito.any())).thenReturn(PasswordReset.class);
    }

    
    @Test
    public void testSendEmailResetButUserDontExists() throws Exception {
        // Arrange
        email = "test@example.com";
        PasswordReset passwordReset = new PasswordReset();
        uuid = UUID.fromString("c1b8fdc2-3227-448c-8e15-8a2d0e9ec9a6");
        passwordReset.setEmail(email);
        passwordReset.setToken(uuid);
        passwordReset.setCreated_at(LocalDateTime.now());
        passwordReset.setExpired_at(LocalDateTime.now().plusMinutes(3));

        Mockito.when(repository.save(passwordReset)).thenReturn(passwordReset);
        assertThrows(RuntimeException.class, () ->  service.sendEmailReset(email));
      
    }

    @Test
    public void testSendEmailResetButTokenIsExpired() throws Exception{
         // Arrange
         email = "test2@example.com";
         uuid = UUID.fromString("c1b8fdc2-3227-448c-8e15-8a2d0e9ec9a5");
         PasswordReset passwordReset = PasswordReset
         .builder()
         .email(email)
         .token(uuid)
         .created_at(LocalDateTime.now())
         .expired_at(LocalDateTime.now())
         .build();

         Mockito.when(repository.save(passwordReset)).thenReturn(passwordReset);
         assertEquals(false, service.validate(passwordReset.getToken().toString(), email, "1234"));
        
    }
    
}
