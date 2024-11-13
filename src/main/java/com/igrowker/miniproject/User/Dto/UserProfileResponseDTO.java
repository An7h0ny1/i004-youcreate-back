package com.igrowker.miniproject.User.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "username", "email", "message", "status"})
public record UserProfileResponseDTO(
        Long id,
        String username,
        String email,
        String message
) {
}
