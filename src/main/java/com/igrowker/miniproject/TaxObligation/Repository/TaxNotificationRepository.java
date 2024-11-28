package com.igrowker.miniproject.TaxObligation.Repository;

import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaxNotificationRepository extends JpaRepository<TaxNotificationEntity, Long> {

    // Buscar notificaciones que no se notifican y tienen como fecha límite antes de una fecha determinada
    List<TaxNotificationEntity> findByIsNotifiedFalseAndTaxDeadlineBefore(LocalDate date);

    // Buscar por ID de usuario
    List<TaxNotificationEntity> findByUser_Id(Long userId);
}
