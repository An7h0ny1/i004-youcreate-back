package com.igrowker.miniproject.User.Model;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FARegister")
public class RegisterVerification2FA{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", length = 512)
    private String token;

    @Column(name = "email")
    private String email;

    @Column(nullable = false)
    private LocalDateTime created_at;

    private LocalDateTime expired_at;

    @Nullable
    private String status;
    

}