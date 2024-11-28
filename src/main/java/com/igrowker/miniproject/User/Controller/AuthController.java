package com.igrowker.miniproject.User.Controller;

import com.igrowker.miniproject.Config.TokenBlacklist;
import com.igrowker.miniproject.TaxObligation.Service.TaxNotificationService;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import com.igrowker.miniproject.User.Dto.AuthCreateUserRequestDto;
import com.igrowker.miniproject.User.Dto.AuthLoginRequestDto;
import com.igrowker.miniproject.User.Dto.AuthResponseDto;
import com.igrowker.miniproject.User.Dto.AuthResponseRegisterDto;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Service.RegisterVerification2FAService;
import com.igrowker.miniproject.User.Service.UserDetailsServiceImpl;
import com.igrowker.miniproject.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication API")
@Validated
public class AuthController {

    private final TokenBlacklist tokenBlacklist;

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RegisterVerification2FAService registerVerification2FAService;
    private final TaxNotificationService taxNotificationService;

    @PostMapping("/login")
    @Tag(name = "Authentication", description = "API for user authentication.")
    @Operation(
            summary = "Login",
            description = "Authenticate a user with their credentials.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credentials required for login",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthLoginRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Login Example",
                                    value = "{ \"email\": \"user123\", \"password\": \"password123\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponseDto.class),
                                    examples = @ExampleObject(
                                            name = "Success Response",
                                            value = "{ \"username\": \"user123\", \"message\": \"User created successfully\", \"jwt\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"status\": true }"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Invalid username or password",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Unauthorized Response",
                                            value = "\"Invalid username or password\""
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "An error occurred during login",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Internal Server Error",
                                            value = "\"An error occurred during login\""
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> login(@RequestBody @Valid AuthLoginRequestDto authDto) {
        AuthResponseDto response = this.userDetailsServiceImpl.loginUser(authDto);
        // Obtener los datos del usuario autenticado
        UserEntity user = userDetailsServiceImpl.getUserByEmail(authDto.getEmail());

        // Activar la lógica de notificación de la fecha límite de impuestos
        taxNotificationService.createOrUpdateTaxNotification(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    @Tag(name = "Authentication", description = "API for user registration.")
    @Operation(
            summary = "Register",
            description = "Register a new user with their credentials.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credentials required for registration",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthCreateUserRequestDto.class)
                    )
            )
    )
    public ResponseEntity<?> register(@RequestBody @Valid AuthCreateUserRequestDto authCreateUserDto) throws Exception {
            AuthResponseRegisterDto response = this.userDetailsServiceImpl.createUser(authCreateUserDto);
            registerVerification2FAService.sendEmailForVerification2FA(authCreateUserDto.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    @Tag(name = "Authentication", description = "API for user authentication.")
    @Operation(
            summary = "Logout",
            description = "Invalidate the user's JWT token to log them out.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(
                                            name = "Success Response",
                                            value = "\"Logout successful\""
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid token",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Response",
                                            value = "\"Invalid token\""
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Obtener el token del encabezado Authorization
        // Se extrae el valor del encabezado "Authorization" de la solicitud HTTP
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
            // Invalidar el token agregándolo a la blacklist (lista negra)
            // Esto significa que el token ya no será válido para futuras solicitudes
            tokenBlacklist.blacklistToken(jwtToken);
            return ResponseEntity.ok("Logout successful");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }
}