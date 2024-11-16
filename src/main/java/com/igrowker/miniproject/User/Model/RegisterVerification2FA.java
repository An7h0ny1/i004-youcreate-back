package com.igrowker.miniproject.User.Model;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Data
@Setter
@Table(name = "2FARegister")
public class RegisterVerification2FA{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "email")
    private String email;

    @Column(nullable = false)
    private LocalDateTime created_at;

    private LocalDateTime expired_at;

    @Nullable
    private String status;
    

}