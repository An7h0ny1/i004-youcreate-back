package com.igrowker.miniproject.TaxObligation.Persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tax_types")
public class TaxType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String taxName; // e.g., "Income Tax", "Sales Tax"

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private BigDecimal percentage; // e.g., 15.5% represented as 15.5

    @Column(nullable = false)
    private BigDecimal baseAmount; // Base amount for calculation, if applicable

}
