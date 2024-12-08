package com.igrowker.miniproject.TaxObligation.Persistence.entity;
import com.igrowker.miniproject.User.Model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "tax_notifications")
public class TaxNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // Relación con UserEntity

    @ManyToOne
    @JoinColumn(name = "tax_type_id", nullable = false)
    private TaxType taxType;

    @Column(nullable = false)
    private LocalDate taxDeadline;// Plazo de pago de impuestos

    @Column(nullable = false)
    private boolean isNotified;

    @Column(nullable = true)
    private LocalDate lastNotifiedDate;


    @Column(nullable = false)
    private boolean paymentConfirmed;


    public LocalDate getLastNotifiedDate() {
        return lastNotifiedDate;
    }

    public void setLastNotifiedDate(LocalDate lastNotifiedDate) {
        this.lastNotifiedDate = lastNotifiedDate;
    }

    public TaxNotificationEntity() {
    }


    // Update constructor to include TaxType
    public TaxNotificationEntity(UserEntity user, TaxType taxType) {
        this.user = user;
        this.taxType = taxType;
        this.taxDeadline = taxType.getExpirationDate();
        this.isNotified = false;
        this.paymentConfirmed = false;
    }
}
