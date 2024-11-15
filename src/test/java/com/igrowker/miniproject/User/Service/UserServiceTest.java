package com.igrowker.miniproject.User.Service;

import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import com.igrowker.miniproject.User.Dto.UserUpdateRequestDTO;
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
                .lastName("testLastName")
                .email("test@gmail.com")
                .phoneNumber("123456789")
                .country("testCountry")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // When
        UserProfileResponseDTO result = userService.getUserProfile(1L);

        // Then
        assertNotNull(result, "El usuario debería existir");
        assertEquals(1L, result.id());
        assertEquals("testUser", result.userName());
        assertEquals("testLastName", result.lastName());
        assertEquals("test@gmail.com", result.email());
        assertEquals("123456789", result.phone());
        assertEquals("testCountry", result.country());
    }

    @Test
    @DisplayName("Test para obtener el perfil de un usuario que no existe")
    public void testGetUserProfile_UserNotFound() {
        // When
        UserProfileResponseDTO result = userService.getUserProfile(2L);

        // Then
        assertEquals("Usuario no encontrado", result.message());
    }

    @Test
    @DisplayName("Test para actualizar el perfil de un usuario")
    public void testUpdateUserProfile_UserExists() {
        // Given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userName("testUser")
                .lastName("testLastName")
                .email("test@gmail.com")
                .phoneNumber("123456789")
                .country("testCountry")
                .build();

        UserUpdateRequestDTO userUpdateRequestDTO = UserUpdateRequestDTO.builder()
                .userName("newTestUser")
                .lastName("newTestLastName")
                .email("test@gmail.com")
                .phoneNumber("123456789")
                .country("testCountry")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // When

        UserProfileResponseDTO result = userService.updateUserProfile(1L, userUpdateRequestDTO);

        // Then
        assertNotNull(result, "El usuario debería existir");
        assertEquals(1L, result.id());
        assertEquals("newTestUser", result.userName());
        assertEquals("newTestLastName", result.lastName());
        assertEquals("test@gmail.com", result.email());
    }
}
