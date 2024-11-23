package com.igrowker.miniproject.TaxObligation.Service;

import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaxService {

    /*
    private static final Map<String, Double> TAX_RATES = Map.of(
            "Bolivia", 14.9,
            "Argentina", 27.0,
            "Dominican Republic", 18.0,
            "Colombia", 19.0,
            "Costa Rica", 13.0,
            "Spain", 21.0
    );

    public Double getTaxRateForCountry(String country) {
        return TAX_RATES.get(country);
    }

     */

    // Constantes internas para países.
    public static final String BOLIVIA = "Bolivia";
    public static final String ARGENTINA = "Argentina";
    public static final String DOMINICAN_REPUBLIC = "Dominican Republic";
    public static final String COLOMBIA = "Colombia";
    public static final String COSTA_RICA = "Costa Rica";
    public static final String SPAIN = "Spain";

    // Tasas IVA asigandas a constantes
    private static final Map<String, Double> TAX_RATES = Map.of(
            BOLIVIA, 14.9,
            ARGENTINA, 27.0,
            DOMINICAN_REPUBLIC, 18.0,
            COLOMBIA, 19.0,
            COSTA_RICA, 13.0,
            SPAIN, 21.0
    );

    // Método para obtener todas los IVA como DTO
    public List<TaxDTO> getAllTaxRates() {
        return TAX_RATES.entrySet().stream()
                .map(entry -> new TaxDTO(entry.getKey(), entry.getValue()))
                .toList();
    }

    // Método para obtener el IVA por país
    public Double getTaxRateForCountry(String country) {
        return TAX_RATES.get(country);
    }
}
