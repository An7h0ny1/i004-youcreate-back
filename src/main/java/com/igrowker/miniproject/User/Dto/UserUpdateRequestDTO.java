package com.igrowker.miniproject.User.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDTO {
    @NotBlank(message = "Username is required")
    public String userName;
    @NotBlank(message = "Last name is required")
    public String lastName;
    @Email(message = "Email should be valid")
    public String email;
    @NotBlank(message = "Phone number is required")
    public String phoneNumber;
    @NotBlank(message = "Country is required")
    public String country;
}
