package com.igrowker.miniproject.User.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "email", "message", "jwt", "status"})
public record AuthResponseDto(Long id,
                              String email,
                              String message,
                              String jwt,
                              boolean status) {
}