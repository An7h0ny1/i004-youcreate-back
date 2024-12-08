package com.igrowker.miniproject.TaxObligation.Repository;

import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxNotificationRepository extends JpaRepository<TaxNotificationEntity, Long> {

}
