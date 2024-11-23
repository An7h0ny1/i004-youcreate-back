package com.igrowker.miniproject.Income.Repository;


import com.igrowker.miniproject.Income.Model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    Optional<Income> findByOrigin(String origin);
    List<Income> findByUserId(Long userId);
}
