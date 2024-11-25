package com.igrowker.miniproject.Collaborator.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.igrowker.miniproject.User.Model.UserEntity;
import jakarta.validation.constraints.NotNull;

@JsonPropertyOrder({"id", "name", "service", "amount", "user_id"})
public record CollaboratorEntityResponseDTO(
        Long id,
        String name,
        String service,
        Double amount,
        Long user_id
) {
}