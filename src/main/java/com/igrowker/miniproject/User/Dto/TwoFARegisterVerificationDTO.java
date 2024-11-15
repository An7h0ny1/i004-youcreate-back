package com.igrowker.miniproject.User.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email", "token"})
public record TwoFARegisterVerificationDTO(
    String email,
    String token
) {
    
}
