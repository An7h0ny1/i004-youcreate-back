package com.igrowker.miniproject.User.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.igrowker.miniproject.User.Model.RegisterVerification2FA;
import java.util.Optional;


public interface RegisterVerification2FARepository extends JpaRepository<RegisterVerification2FA, Long>{
    Optional<RegisterVerification2FA> findByEmail(String email);
}