package com.igrowker.miniproject.TaxObligation.Dto;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxDTO {
    //private Long taxId;
    private String taxName;        // e.g., VAT, ISR
    private double amount;
    private double percentage;     // Tax percentage
    private LocalDate deadline;    // Expiration date
    private String status;         // e.g., PENDING
    private String action;         // e.g., "pay"
    private double taxDue;         // Monto del impuesto adeudado por este impuesto en particular (valor calculado)
    private double amountPaid; // Refleja el monto pagado

    // getters, and setters

    // Getters and Setters
    public double getTaxDue() {
        return taxDue;
    }

    public void setTaxDue(double taxDue) {
        this.taxDue = taxDue;
    }         // e.g., "pagar"
}



