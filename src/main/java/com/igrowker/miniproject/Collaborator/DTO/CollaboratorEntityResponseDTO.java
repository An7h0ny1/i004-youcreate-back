package com.igrowker.miniproject.Collaborator.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name", "service", "date", "amount", "user_id"})
public record CollaboratorEntityResponseDTO(
        Long id,
        String name,
        String service,
        String date,
        Double amount,
        Long user_id
) {
}