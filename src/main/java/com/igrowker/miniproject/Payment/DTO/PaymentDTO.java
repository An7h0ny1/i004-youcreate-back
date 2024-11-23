package com.igrowker.miniproject.Payment.DTO;

import com.igrowker.miniproject.Payment.Model.PaymentMethod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentDTO{
    
    
    @Min(value = 0, message = "el id debe ser mayor o igual a 0")
    private Long collaborator_id;

    @NotNull(message = "Category is required")
    private PaymentMethod category;
}
