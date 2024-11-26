package com.igrowker.miniproject.Income.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeUpdateRequestDTO {

    @Positive(message = "Amount must be positive")
    public Long id;

    @Positive(message = "Amount must be positive")
    public double amount;
    @NotBlank(message = "Origin is required")
    public String origin;
    @NotNull(message = "Date is required")
    public String date;
    @NotBlank(message = "Category is required")
    public String category;
}
