package com.igrowker.miniproject.TaxObligation.Repository;

import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface TaxTypeRepository extends JpaRepository<TaxType, Long> {

    List<TaxType> findByCountry(String country);
    List<TaxType> findByExpirationDateBefore(LocalDate date);
    Optional<TaxType> findByCountryAndTaxName(String country, String taxName);

    Optional<TaxType> findByTaxName(String taxName);
}
