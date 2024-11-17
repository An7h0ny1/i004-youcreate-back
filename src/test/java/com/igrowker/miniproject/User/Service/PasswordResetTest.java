package com.igrowker.miniproject.User.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.igrowker.miniproject.User.Exception.PasswordMismatchException;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.PasswordReset;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.PasswordResetRepository;
import com.igrowker.miniproject.User.Repository.UserRepository;

@SpringBootTest
public class PasswordResetTest {

    @Autowired
    PasswordResetRepository repository;

    @Autowired
    PasswordResetService service;

    @Autowired
    private UserRepository userRepository;

    private String email;
    private UUID uuid;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendEmailResetOk() throws Exception {
        email = "test1@example.com";
        UserEntity user = UserEntity
                .builder()
                .email(email)
                .build();

        assertDoesNotThrow(() -> {
            userRepository.save(user);
            service.sendEmailReset(email);
        });

    }

    @Test
    public void testSendEmailResetButUserDontExists() throws Exception {
        assertThrows(UserNotFoundException.class, () -> service.sendEmailReset(email));
    }

    @Test
    public void testVerifyResetButTokenIsExpired() throws Exception {
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

        repository.save(passwordReset);
        assertEquals(false, service.validate(passwordReset.getToken().toString(), email, "1234"));

    }

    @Test
    public void testVerifyResetButTokenIsInvalid() throws Exception {
        // Arrange
        email = "test3@example.com";
        uuid = UUID.fromString("c1b8fdc2-3227-448c-8e15-8a2d0e9ec9a5");
        PasswordReset passwordReset = PasswordReset
                .builder()
                .email(email)
                .token(uuid)
                .created_at(LocalDateTime.now())
                .expired_at(LocalDateTime.now().plusMinutes(3))
                .build();

            repository.save(passwordReset);
        assertEquals(false,
                service.validate(UUID.fromString("ae99d51e-9c12-4fac-bbdf-8f3425aee791").toString(), email, "1234"));

    }

    @Test
    public void testVerifyResetButTokenIsUsed() throws Exception {
        // Arrange
        email = "test4@example.com";
        uuid = UUID.fromString("c1b8fdc2-3227-448c-8e15-8a2d0e9ec9a5");
        PasswordReset passwordReset = PasswordReset
                .builder()
                .email(email)
                .token(uuid)
                .created_at(LocalDateTime.now())
                .expired_at(LocalDateTime.now())
                .status("USED")
                .build();
        
        userRepository.save(UserEntity.builder().email(email).build());
        repository.save(passwordReset);
        assertThrows(IllegalStateException.class,
                () -> service.validate(passwordReset.getToken().toString(), email, "1234"));

    }

    @Test
    public void testVerifyResetButResetDoesntExists() throws Exception {
        // Arrange
        email = "test5@example.com";
        uuid = UUID.fromString("c1b8fdc2-3227-448c-8e15-8a2d0e9ec9a5");
     
        assertThrows(PasswordMismatchException.class,
                () -> service.validate(uuid.toString(), email, "1234"));

    }

    @Test
    public void testVerifyResetButUserDoesntExists() throws Exception {
        // Arrange
        email = "test6@example.com";
        uuid = UUID.fromString("c1b8fdc2-3227-448c-8e15-8a2d0e9ec9a5");
        PasswordReset passwordReset = PasswordReset
                .builder()
                .email(email)
                .token(uuid)
                .created_at(LocalDateTime.now())
                .expired_at(LocalDateTime.now().plusMinutes(3))
                .build();
        
    
        repository.save(passwordReset);
        assertThrows(NoSuchElementException.class,
                () -> service.validate(uuid.toString(), email, "1234"));

    }


}
