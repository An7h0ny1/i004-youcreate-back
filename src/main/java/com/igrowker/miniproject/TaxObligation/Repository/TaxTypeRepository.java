package com.igrowker.miniproject.TaxObligation.Repository;

import com.igrowker.miniproject.TaxObligation.Dto.TaxCategory;
import com.igrowker.miniproject.TaxObligation.Dto.TaxStatus;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.User.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface TaxTypeRepository extends JpaRepository<TaxType, Long> {


    List<TaxType> findByUserId(Long userId);

    List<TaxType> findAllByStatus(TaxStatus taxStatus);

    List<TaxType> findByUserAndStatus(UserEntity user, TaxStatus status);

    Optional<Object> findByUserIdAndCategory(Long userId, TaxCategory taxCategory);

}
