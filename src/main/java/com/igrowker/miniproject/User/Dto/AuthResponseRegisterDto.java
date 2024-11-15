package com.igrowker.miniproject.User.Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email", "message", "jwt", "status"})
public record AuthResponseRegisterDto(String email,
                                      String message,
                                      String jwt,
                                      boolean status) {
}