package com.igrowker.miniproject.User.Dto;

import com.igrowker.miniproject.User.Model.Enum.EnumCountry;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthCreateUserRequestDto(@NotBlank(message = "Username cannot be blank") String userName,
                                       @NotBlank(message = "Last name cannot be blank") String lastName,
                                       @NotBlank(message = "Email cannot be blank") @Email(message = "Email must be valid") String email,
                                       @NotBlank(message = "Password cannot be blank") String password,
                                       @NotNull(message = "Country cannot be null") EnumCountry country) {
}