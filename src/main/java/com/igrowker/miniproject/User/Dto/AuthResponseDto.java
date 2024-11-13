package com.igrowker.miniproject.User.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "username", "message", "jwt", "status"})
public record AuthResponseDto(Long id,
                              String username,
                              String message,
                              String jwt,
                              boolean status) {
}