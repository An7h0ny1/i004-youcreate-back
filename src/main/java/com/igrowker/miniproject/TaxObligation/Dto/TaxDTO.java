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
    private boolean isNotified;

    public TaxDTO(Long userId, String userName, String country, LocalDate taxDeadline, boolean isNotified) {
        this.userId = userId;
        this.userName = userName;
        this.country = country;
        this.taxDeadline = taxDeadline;
        this.isNotified = isNotified;
    }

}
