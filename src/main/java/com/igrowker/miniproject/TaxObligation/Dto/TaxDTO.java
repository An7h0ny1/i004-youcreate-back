package com.igrowker.miniproject.TaxObligation.Dto;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
public class TaxDTO {

    private Long userId;
    private String userName;
    private String country;
    private LocalDate taxDeadline;
    private LocalDate lastNotifiedDate;
    private boolean isPaymentConfirmed = false;

    public TaxDTO(Long userId, String userName, String country, LocalDate taxDeadline, LocalDate lastNotifiedDate, boolean isPaymentConfirmed) {
        this.userId = userId;
        this.userName = userName;
        this.country = country;
        this.taxDeadline = taxDeadline;
        this.lastNotifiedDate = lastNotifiedDate;
        this.isPaymentConfirmed = isPaymentConfirmed;
    }

}
