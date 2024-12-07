package com.igrowker.miniproject.Payment.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Entity
@Data
@Table(name = "payment")
@Nonnull
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    //@Min(value = 0, message = "el id debe ser mayor o igual a 0")
    private Long id;

    @Column(nullable = false, name = "collaborator_id")
    private Long collaboratorId;

    @DecimalMin(value = "0.1", message = "el monto debe ser mayor o igual a 0")
    @Column(name = "amount")
    @NotNull
    private Double amount;

    @PastOrPresent
    @Column(name = "date_payment", nullable = false)
    private LocalDate date;

    @Column(name = "date_expired", nullable = false)
    private LocalDate expired_date;

    @Column(name = "service_name", nullable = false)
    private String service;

    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "category", nullable = false)
    private PaymentMethod category;

}
