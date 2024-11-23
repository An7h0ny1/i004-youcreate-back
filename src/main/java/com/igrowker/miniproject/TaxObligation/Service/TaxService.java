package com.igrowker.miniproject.TaxObligation.Service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TaxService {

    private static final Map<String, Double> TAX_RATES = Map.of(
            "Bolivia", 14.9,
            "Argentina", 27.0,
            "Dominican Republic", 18.0,
            "Colombia", 19.0,
            "Costa Rica", 13.0,
            "Spain", 21.0
    );

    /*
    public Double getTaxRate(String country) {
        return TAX_RATES.getOrDefault(country, 0.0); // Por Defecto 0 si el pais no es encontrado
    }

    public Map<String, Double> getAllTaxRates() {
        return TAX_RATES;
    }
     */

    public Double getTaxRateForCountry(String country) {
        return TAX_RATES.get(country);
    }
}
