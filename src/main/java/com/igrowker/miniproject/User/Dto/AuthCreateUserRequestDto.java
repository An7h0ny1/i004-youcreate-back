package com.igrowker.miniproject.User.Dto;

import com.igrowker.miniproject.User.Model.Enum.EnumCountry;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthCreateUserRequestDto {

    @NotBlank(message = "El nombre de usuario es requerido")
    private String userName;
    @NotBlank(message = "El Apellido es requerido")
    private String lastName;
    @NotBlank(message = "El numero de telefono es requerido")
    private String phoneNumber;
    @NotBlank(message = "La contraseña es requerida")
    private String password;
    @NotBlank(message = "La confirmacion de la contraseña es requerida")
    private String confirmPassword;
    @NotBlank(message = "El email es requerido")
    @Email
    private String email;
    @NotBlank(message = "El pais es requerido")
    private String country;
}