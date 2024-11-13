package com.igrowker.miniproject.User.Dto;


import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequestDto(@NotBlank(message = "Username is required") String email,
                                  @NotBlank(message = "Password is required") String password) {
}
