package com.igrowker.miniproject.User.Repository;

import org.springframework.stereotype.Repository;

import com.igrowker.miniproject.User.Model.PasswordReset;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;





@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long>{
    Optional<PasswordReset> findByEmail(String email);
  
}
