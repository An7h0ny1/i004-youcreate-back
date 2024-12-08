package com.igrowker.miniproject.TaxObligation.Config;

import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final TaxService taxService;

    //@Scheduled(fixedRate = 5000)
    @Scheduled(cron = "0 0 9 * * ?") // Funciona todos los días a las 9 a.m.
    public void runNotificationCheck() {
        taxService.sendDailyTaxNotifications();
    }
}
