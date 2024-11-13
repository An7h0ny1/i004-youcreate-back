package com.igrowker.miniproject.User.Service;

import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test para obtener el perfil de un usuario")
    public void testGetUserProfile_UserExists() {
        // Given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName("testUser")
                .email("test@gmail.com")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // When
        UserEntity result = userService.getUserProfile(1L);

        // Then
        assertNotNull(result, "El usuario debería existir");
        assertEquals(1L, result.getId());
        assertEquals("testUser", result.getUserName());
        assertEquals("test@gmail.com", result.getEmail());
    }

    @Test
    @DisplayName("Test para obtener el perfil de un usuario que no existe")
    public void testGetUserProfile_UserNotFound() {
        // When
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        UserEntity result = userService.getUserProfile(2L);

        // Then
        assertNull(result, "El resultado debería ser null si el usuario no existe");
    }
}
