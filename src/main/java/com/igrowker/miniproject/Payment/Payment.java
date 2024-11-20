package com.igrowker.miniproject.Payment;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

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

    @Min(value = 0, message = "el monto debe ser mayor o igual a 0")
    @Column(name = "amount")
    private double amount;

    @PastOrPresent
    @Column(name = "date_payment")
    private Instant date;

    @Column(name = "service_name")
    private String service;

    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "category")
    private PaymentMethod category;
}
