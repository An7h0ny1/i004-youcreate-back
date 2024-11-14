package com.igrowker.miniproject.User.Dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthLoginRequestDto {

    @NotBlank(message = "El email es requerido")
    private String email;
    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
