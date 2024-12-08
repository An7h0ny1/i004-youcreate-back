package com.igrowker.miniproject.TaxObligation.Service;
import com.igrowker.miniproject.Income.Repository.IncomeRepository;
import com.igrowker.miniproject.Income.Service.IncomeService;
import com.igrowker.miniproject.TaxObligation.Dto.TaxCategory;
import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Dto.TaxStatus;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.IncomeBalance;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.TaxObligation.Repository.IncomeBalanceRepository;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import com.igrowker.miniproject.TaxObligation.Repository.TaxTypeRepository;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import com.igrowker.miniproject.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaxService {

    private final TaxNotificationService taxNotificationService;
    private final TaxNotificationEmailService taxNotificationEmailService;
    private final TaxNotificationRepository taxNotificationRepository;
    private final TaxTypeRepository taxTypeRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final IncomeService incomeService;
    private final IncomeRepository incomeRepository;
    private final IncomeBalanceRepository incomeBalanceRepository;

    @Autowired
    public TaxService(TaxNotificationService taxNotificationService, TaxNotificationEmailService taxNotificationEmailService, TaxNotificationRepository taxNotificationRepository, TaxTypeRepository taxTypeRepository, UserService userService, UserRepository userRepository, IncomeService incomeService, IncomeRepository incomeRepository, IncomeBalanceRepository incomeBalanceRepository) {
        this.taxNotificationService = taxNotificationService;
        this.taxNotificationEmailService = taxNotificationEmailService;
        this.taxNotificationRepository = taxNotificationRepository;
        this.taxTypeRepository = taxTypeRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.incomeService = incomeService;
        this.incomeRepository = incomeRepository;
        this.incomeBalanceRepository = incomeBalanceRepository;

    }

    private static final Map<String, List<TaxType>> TAX_CONFIG = Map.of(
            "Bolivia", List.of(
                    new TaxType(null, TaxCategory.VAT, 13.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 15.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Argentina", List.of(
                    new TaxType(null, TaxCategory.VAT, 27.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 29.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Colombia", List.of(
                    new TaxType(null, TaxCategory.VAT, 19.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 25.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Dominican Republic", List.of(
                    new TaxType(null, TaxCategory.VAT, 18.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 24.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Costa Rica", List.of(
                    new TaxType(null, TaxCategory.VAT, 13.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 18.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Spain", List.of(
                    new TaxType(null, TaxCategory.VAT, 21.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 25.0, LocalDate.of(2024, 12, 12), TaxStatus.PENDING, null, 0.0, 0.0)
            )
            // Agregue los países restantes aquí
    );

    public List<TaxDTO> calculateTaxes(Long userId) {
        // Recuperar el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        // Obtenga el saldo de ingresos actual de IncomeBalance
        BigDecimal totalIncome = incomeBalanceRepository.findByUserId(userId)
                .map(IncomeBalance::getCurrentBalance)
                .orElseThrow(() -> new IllegalStateException("Income balance not initialized for user " + userId));

        // Obtener tasas impositivas para el país del usuario
        String country = user.getCountry();
        List<TaxType> taxRates = TAX_CONFIG.getOrDefault(country, List.of());


        List<TaxDTO> taxDetails = new ArrayList<>();

        for (TaxType tax : taxRates) {
            if (tax.getStatus() == TaxStatus.PAID) {
                taxDetails.add(new TaxDTO(
                        tax.getCategory().name(),
                        totalIncome.doubleValue(),
                        tax.getPercentage(),
                        tax.getExpirationDate(),
                        "PAID",
                        "Paid",
                        0,
                        tax.getAmountPaid()
                ));
            } else {
                BigDecimal taxDue = totalIncome.multiply(BigDecimal.valueOf(tax.getPercentage()).divide(BigDecimal.valueOf(100)));
                taxDetails.add(new TaxDTO(
                        tax.getCategory().name(),
                        totalIncome.doubleValue(),
                        tax.getPercentage(),
                        tax.getExpirationDate(),
                        "PENDING",
                        "Pay",
                        taxDue.doubleValue(),
                        0
                ));
            }
        }

        return taxDetails;
    }

    public void updateTaxDetailsInDatabase(List<TaxDTO> taxes, Long userId) {
        for (TaxDTO tax : taxes) {
            // Convertir el nombre del impuesto (cadena) de DTO a la enumeración TaxCategory
            TaxCategory taxCategory;
            try {
                taxCategory = TaxCategory.valueOf(tax.getTaxName().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Invalid tax category: " + tax.getTaxName(), e);
            }

            // Obtener la entidad TaxType utilizando el método de repositorio actualizado
            TaxType taxEntity = (TaxType) taxTypeRepository.findByUserIdAndCategory(userId, taxCategory)
                    .orElseThrow(() -> new IllegalStateException("Tax not found for category: " + tax.getTaxName()));

            // Actualizar campos en la entidad
            taxEntity.setStatus(TaxStatus.valueOf(tax.getStatus().toUpperCase())); // Garantizar el mapeo de enumeración correcto
            taxEntity.setAmountPaid(tax.getAmountPaid());
            taxEntity.setTaxDue(tax.getTaxDue());

            // Guarde la entidad actualizada
            taxTypeRepository.save(taxEntity);
        }
    }


    public void associateTaxesToUser(UserEntity user) {
        List<TaxType> countryTaxes = TAX_CONFIG.get(user.getCountry());
        if (countryTaxes == null) {
            throw new IllegalArgumentException("No taxes defined for country: " + user.getCountry());
        }

        List<TaxType> userTaxes = countryTaxes.stream()
                .map(tax -> TaxType.builder()
                        .category(tax.getCategory())
                        .percentage(tax.getPercentage())
                        .expirationDate(tax.getExpirationDate())
                        .status(TaxStatus.PENDING)
                        .user(user)
                        .build())
                .collect(Collectors.toList());

        taxTypeRepository.saveAll(userTaxes);
    }

    public List<TaxDTO> getTaxesForUser(Long userId) {
        // Recuperar el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        // Obtenga el saldo de ingresos actual de IncomeBalance
        BigDecimal totalIncome = incomeBalanceRepository.findByUserId(userId)
                .map(IncomeBalance::getCurrentBalance)
                .orElseThrow(() -> new IllegalStateException("Income balance not initialized for user " + userId));

        // Recuperar impuestos para el usuario.
        List<TaxType> userTaxes = taxTypeRepository.findByUserId(userId);

        return userTaxes.stream()
                .map(tax -> {
                    if (tax.getStatus() == TaxStatus.PAID) {
                        return new TaxDTO(
                                tax.getCategory().name(),
                                totalIncome.doubleValue(),
                                tax.getPercentage(),
                                tax.getExpirationDate(),
                                "PAID",
                                "Paid",
                                0,
                                tax.getAmountPaid()
                        );
                    } else {
                        BigDecimal taxDue = totalIncome.multiply(BigDecimal.valueOf(tax.getPercentage()).divide(BigDecimal.valueOf(100)));
                        return new TaxDTO(
                                tax.getCategory().name(),
                                totalIncome.doubleValue(),
                                tax.getPercentage(),
                                tax.getExpirationDate(),
                                "PENDING",
                                "Pay",
                                taxDue.doubleValue(),
                                0
                        );
                    }
                })
                .collect(Collectors.toList());
    }



    @Scheduled(cron = "0 0 9 * * ?") // Funciona todos los días a las 9 a.m.
    public void sendDailyTaxNotifications() {
        List<TaxType> pendingTaxes = taxTypeRepository.findAllByStatus(TaxStatus.PENDING);

        for (TaxType tax : pendingTaxes) {
            LocalDate today = LocalDate.now();
            long daysUntilDeadline = ChronoUnit.DAYS.between(today, tax.getExpirationDate());
            UserEntity user = tax.getUser();

            if (daysUntilDeadline == 4) {
                // Notificar 4 días antes del plazo
                taxNotificationEmailService.sendTaxDeadlineNotification(user.getEmail(), user.getCountry(), tax.getExpirationDate());
            } else if (daysUntilDeadline > 0) {
                // Notificar diariamente hasta la fecha límite si no se paga
                taxNotificationEmailService.sendDailyPaymentReminder(user.getEmail(), tax);
            }
        }
    }

    public void payVAT(Long userId) {
        paySpecificTax(userId, TaxCategory.VAT);
    }

    public void payISR(Long userId) {
        paySpecificTax(userId, TaxCategory.ISR);
    }

    public void sendLoginTaxNotifications(UserEntity user) {
        List<TaxType> pendingTaxes = taxTypeRepository.findByUserAndStatus(user, TaxStatus.PENDING);

        for (TaxType tax : pendingTaxes) {
            LocalDate today = LocalDate.now();
            long daysUntilDeadline = ChronoUnit.DAYS.between(today, tax.getExpirationDate());

            if (daysUntilDeadline == 4) {
                // Notificar 4 días antes del plazo
                taxNotificationEmailService.sendTaxDeadlineNotification(user.getEmail(), user.getCountry(), tax.getExpirationDate());
            } else if (daysUntilDeadline > 0) {
                // Notificar diariamente hasta la fecha límite si no se paga
                taxNotificationEmailService.sendDailyPaymentReminder(user.getEmail(), tax);
            }
        }
    }

    public void paySpecificTax(Long userId, TaxCategory taxCategory) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        // Recuperar el saldo de ingresos para el usuario.
        IncomeBalance incomeBalance = incomeBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Income balance not initialized for user"));

        // Recuperar impuestos para el usuario.
        List<TaxDTO> taxes = calculateTaxes(userId);

        // Encuentre el impuesto seleccionado según la categoría
        TaxDTO paidTax = taxes.stream()
                .filter(tax -> tax.getTaxName().equalsIgnoreCase(taxCategory.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid tax category: " + taxCategory));

        // Verificar si el impuesto seleccionado ya está pagado
        if (paidTax.getStatus().equalsIgnoreCase("PAID")) {
            throw new IllegalStateException("Tax " + taxCategory.name() + " is already paid.");
        }

        // Marcar el impuesto seleccionado como pagado
        double taxDue = paidTax.getTaxDue();
        paidTax.setStatus("PAID");
        paidTax.setAmountPaid(taxDue);
        paidTax.setTaxDue(0);

        // Actualizar el balance de ingresos
        BigDecimal remainingBalance = incomeBalance.getCurrentBalance().subtract(BigDecimal.valueOf(taxDue));
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Insufficient income balance to pay tax.");
        }
        incomeBalance.setCurrentBalance(remainingBalance);

        // Conservar el saldo actualizado
        incomeBalanceRepository.save(incomeBalance);

        // Actualizar los detalles del impuesto en la base de datos (solo para el impuesto pagado)
        updateTaxDetailsInDatabase(List.of(paidTax), userId);
    }

    public void initializeIncomeBalance(Long userId) {
        BigDecimal totalIncome = incomeService.getTotalAmount(userId); // Ahora se refiere al servicio correcto.
        IncomeBalance balance = incomeBalanceRepository.findByUserId(userId)
                .orElse(new IncomeBalance());
        balance.setUser(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found")));
        balance.setCurrentBalance(totalIncome);
        incomeBalanceRepository.save(balance);
    }


    public double calculateTotalPayments(Long userId) {
        // Obtener todos los impuestos para el usuario.
        List<TaxDTO> taxes = getTaxesForUser(userId);

        // Calcular la suma del monto pagado por impuestos con estado "PAID"
        return taxes.stream()
                .filter(tax -> tax.getStatus().equalsIgnoreCase("PAID"))
                .mapToDouble(TaxDTO::getAmountPaid)
                .sum();
    }

}
