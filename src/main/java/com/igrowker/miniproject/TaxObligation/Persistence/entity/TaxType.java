package com.igrowker.miniproject.TaxObligation.Persistence.entity;

import com.igrowker.miniproject.TaxObligation.Dto.TaxCategory;
import com.igrowker.miniproject.TaxObligation.Dto.TaxStatus;
import com.igrowker.miniproject.User.Model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tax_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxCategory category;

    @Column(nullable = false)
    private double percentage;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxStatus status; // e.g., PENDING, PAID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private double amountPaid;

    private double taxDue;         // Monto a pagar (persistió)

    // Getters and Setters
    public double getTaxDue() {
        return taxDue;
    }

    public void setTaxDue(double taxDue) {
        this.taxDue = taxDue;
    }

    // Getters and setters
    public void payTax(double amount) {
        this.status = TaxStatus.PAID;
        this.amountPaid = amount;  // Establecer el monto pagado
    }

    public double getAmountPaid() {
        return this.amountPaid;
    }
}
