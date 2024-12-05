package com.igrowker.miniproject.TaxObligation.Dto;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
public class TaxDTO {

    private Long userId;                  // ID of the user
    private String userName;              // Name of the user
    private String country;               // Country (from TaxType)
    private String taxName;               // Tax name (from TaxType)
    private LocalDate taxDeadline;        // Tax deadline (from TaxType)
    private LocalDate lastNotifiedDate;   // Last notification date
    private boolean isPaymentConfirmed;   // Payment confirmation status

    public TaxDTO(Long userId, String userName, String country, String taxName, LocalDate taxDeadline, LocalDate lastNotifiedDate, boolean isPaymentConfirmed) {
        this.userId = userId;
        this.userName = userName;
        this.country = country;
        this.taxName = taxName;
        this.taxDeadline = taxDeadline;
        this.lastNotifiedDate = lastNotifiedDate;
        this.isPaymentConfirmed = isPaymentConfirmed;
    }

}
