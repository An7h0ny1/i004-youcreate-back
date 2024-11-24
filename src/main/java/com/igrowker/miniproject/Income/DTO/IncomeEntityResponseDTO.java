package com.igrowker.miniproject.Income.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

    @JsonPropertyOrder({"id", "amount", "origin", "date", "user_id"})
    public record IncomeEntityResponseDTO(
            Long id,
            double amount,
            String origin,
            Date date,
            Long user_id
    ) {
    }

