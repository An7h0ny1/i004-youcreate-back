package com.igrowker.miniproject.User.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "country", "userName", "lastName", "email", "phone", "country", "message"})
public record UserProfileResponseDTO(
        Long id,
        String userName,
        String lastName,
        String email,
        String phone,
        String country,
        String message
) {
}
