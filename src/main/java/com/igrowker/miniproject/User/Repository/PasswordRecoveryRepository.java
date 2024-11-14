package com.igrowker.miniproject.PasswordRecovery.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.igrowker.miniproject.PasswordRecovery.Model.PasswordRecovery;



@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long>{
    
}
