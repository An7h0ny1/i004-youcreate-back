package com.igrowker.miniproject.Collaborator.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollaboratorUpdateRequestDTO {
    @NotBlank(message = "Name is required")
    public String name;
    @NotBlank(message = "Service is required")
    public String service;
    @NotBlank(message = "Amount is required")
    public Double amount;
}
