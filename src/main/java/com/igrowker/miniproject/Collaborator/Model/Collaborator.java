package com.igrowker.miniproject.Collaborator.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.igrowker.miniproject.Payment.Model.Payment;
import com.igrowker.miniproject.User.Model.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "collaborators")
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Service is required")
    @Column(name = "service")
    private String service;

    @NotNull(message = "Amount is required")
    @Column(name = "amount")
    private Double amount;
    
    @NotNull(message = "Date is required")
    @Column(name = "date")
    private String date;
    
    @NotNull(message = "User is required")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity user;

}
