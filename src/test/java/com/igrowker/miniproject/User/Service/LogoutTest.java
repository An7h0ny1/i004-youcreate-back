package com.igrowker.miniproject.User.Service;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.*;
import com.igrowker.miniproject.Config.TokenBlacklist;
import com.igrowker.miniproject.User.Controller.AuthController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class LogoutTest {

    @InjectMocks
    private AuthController authController; // Cambia esto por el nombre de tu controlador

    @Mock
    private TokenBlacklist tokenBlacklist;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogout_Success() {
        // Preparamos el escenario para la prueba
        String token = "some.jwt.token";
        // Simulamos que el encabezado Authorization contiene el token
        when(request.getHeader(AUTHORIZATION)).thenReturn("Bearer " + token);

        ResponseEntity<?> response = authController.logout(request);

        // Verificamos que se llame al método blacklistToken con el token correcto
        verify(tokenBlacklist).blacklistToken(token);
        assertEquals(OK, response.getStatusCode());
        assertEquals("Logout successful", response.getBody());
    }

    @Test
    public void testLogout_InvalidToken() {
        // Arrange
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        // Act
        ResponseEntity<?> response = authController.logout(request);

        // Assert
        verify(tokenBlacklist, never()).blacklistToken(anyString()); // Verifica que no se llame a blacklistToken
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }
}
