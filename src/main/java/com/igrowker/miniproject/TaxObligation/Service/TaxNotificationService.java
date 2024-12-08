package com.igrowker.miniproject.TaxObligation.Service;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import com.igrowker.miniproject.TaxObligation.Repository.TaxTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxNotificationService {

    private final TaxNotificationRepository taxNotificationRepository;
    private final TaxNotificationEmailService taxNotificationEmailService;
    private final TaxTypeRepository taxTypeRepository;


    @Autowired
    public TaxNotificationService(TaxNotificationRepository taxNotificationRepository, TaxNotificationEmailService taxNotificationEmailService, TaxTypeRepository taxTypeRepository) {
        this.taxNotificationRepository = taxNotificationRepository;
        this.taxNotificationEmailService = taxNotificationEmailService;
        this.taxTypeRepository = taxTypeRepository;
    }
}
