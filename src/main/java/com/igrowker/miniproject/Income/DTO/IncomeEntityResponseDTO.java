package com.igrowker.miniproject.Income.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.Date;

    @JsonPropertyOrder({"id", "amount", "origin", "date", "category", "user_id"})
    public record IncomeEntityResponseDTO(
            Long id,
            double amount,
            String origin,
            String date,
            String category,
            Long user_id
    ) {
    }

