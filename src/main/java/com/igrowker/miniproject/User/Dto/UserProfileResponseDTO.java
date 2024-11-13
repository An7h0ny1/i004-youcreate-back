package com.igrowker.miniproject.User.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "country", "username", "email", "message"})
public record UserProfileResponseDTO(
        Long id,
        String country,
        String username,
        String email,
        String message

) {
}
