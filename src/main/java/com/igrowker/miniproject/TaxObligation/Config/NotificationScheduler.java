package com.igrowker.miniproject.TaxObligation.Config;

import com.igrowker.miniproject.TaxObligation.Service.TaxNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final TaxNotificationService taxNotificationService;

    //@Scheduled(cron = "0 0 9 * * ?") // Funciona todos los días a las 9 a.m.
    @Scheduled(fixedRate = 5000)
    public void runNotificationCheck() {
        taxNotificationService.notifyUsersAboutUpcomingDeadlines();
    }
}
