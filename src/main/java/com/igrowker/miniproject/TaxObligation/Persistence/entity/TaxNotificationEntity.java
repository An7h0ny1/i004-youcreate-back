package com.igrowker.miniproject.TaxObligation.Persistence.entity;

import com.igrowker.miniproject.User.Model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "tax_notifications")
public class TaxNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // Relación con UserEntity

    @Column(nullable = false)
    private String country; // País donde se aplica el impuesto

    @Column(nullable = false)
    private LocalDate taxDeadline; // Plazo de pago de impuestos

    @Column(nullable = false)
    private boolean isNotified; // Seguimiento si el usuario ha sido notificado



    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) { // Follow naming conventions
        this.isNotified = notified;
    }

    public TaxNotificationEntity() {
    }

    public TaxNotificationEntity(UserEntity user, String country, LocalDate taxDeadline) {
        this.user = user;
        this.country = country;
        this.taxDeadline = taxDeadline;
    }

}
