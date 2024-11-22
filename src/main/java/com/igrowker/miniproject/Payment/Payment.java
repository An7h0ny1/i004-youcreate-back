package com.igrowker.miniproject.Payment;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "payment")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Min(value = 0, message = "el id debe ser mayor o igual a 0")
    private Long id;

    @Column(nullable = false, name = "collaborator_id")
    private Long collaborator_id;

    @DecimalMin(value = "0.1", message = "el monto debe ser mayor o igual a 0")
    @Column(name = "amount")
    @NotNull
    private Double amount;

    @PastOrPresent
    @Column(name = "date_payment", nullable = false)
    private Instant date;

    @Column(name = "service_name", nullable = false)
    private String service;

    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "category", nullable = false)
    private PaymentMethod category;
}
