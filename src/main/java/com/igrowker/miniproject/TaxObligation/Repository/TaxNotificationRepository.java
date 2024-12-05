package com.igrowker.miniproject.TaxObligation.Repository;

import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.User.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxNotificationRepository extends JpaRepository<TaxNotificationEntity, Long> {

    // Buscar notificaciones que no se notifican y tienen como fecha límite antes de una fecha determinada
    List<TaxNotificationEntity> findByIsNotifiedFalseAndTaxDeadlineBefore(LocalDate date);

    // Buscar por ID de usuario
    List<TaxNotificationEntity> findByUser_Id(Long userId);

    List<TaxNotificationEntity> findByTaxDeadlineBetween(LocalDate now, LocalDate localDate);


    List<TaxNotificationEntity> findByIsNotifiedFalseAndTaxDeadlineIn(List<LocalDate> list);

    Optional<TaxNotificationEntity> findByUserAndTaxType(UserEntity user, TaxType taxType);

    List<TaxNotificationEntity> findByTaxTypeAndTaxDeadlineBetween(TaxType taxType, LocalDate startDate, LocalDate endDate);

}
