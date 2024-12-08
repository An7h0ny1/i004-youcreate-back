package com.igrowker.miniproject.TaxObligation.Repository;

import com.igrowker.miniproject.TaxObligation.Persistence.entity.IncomeBalance;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncomeBalanceRepository extends JpaRepository<IncomeBalance, Long> {

    Optional<IncomeBalance> findByUserId(Long userId);

}
