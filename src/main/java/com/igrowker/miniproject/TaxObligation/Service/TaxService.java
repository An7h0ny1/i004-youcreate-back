package com.igrowker.miniproject.TaxObligation.Service;

import com.igrowker.miniproject.Income.DTO.IncomeEntityResponseDTO;
import com.igrowker.miniproject.Income.Model.Income;
import com.igrowker.miniproject.Income.Repository.IncomeRepository;
import com.igrowker.miniproject.Income.Service.IncomeService;
import com.igrowker.miniproject.TaxObligation.Dto.TaxCategory;
import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Dto.TaxStatus;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxNotificationEntity;
import com.igrowker.miniproject.TaxObligation.Persistence.entity.TaxType;
import com.igrowker.miniproject.TaxObligation.Repository.TaxNotificationRepository;
import com.igrowker.miniproject.TaxObligation.Repository.TaxTypeRepository;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Repository.UserRepository;
import com.igrowker.miniproject.User.Service.UserService;
import jakarta.transaction.Transactional;
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

    @Autowired
    public TaxService(TaxNotificationService taxNotificationService, TaxNotificationEmailService taxNotificationEmailService, TaxNotificationRepository taxNotificationRepository, TaxTypeRepository taxTypeRepository, UserService userService, UserRepository userRepository, IncomeService incomeService, IncomeRepository incomeRepository) {
        this.taxNotificationService = taxNotificationService;
        this.taxNotificationEmailService = taxNotificationEmailService;
        this.taxNotificationRepository = taxNotificationRepository;
        this.taxTypeRepository = taxTypeRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.incomeService = incomeService;
        this.incomeRepository = incomeRepository;

    }

    private static final Map<String, List<TaxType>> TAX_CONFIG = Map.of(
            "Bolivia", List.of(
                    new TaxType(null, TaxCategory.VAT, 13.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 15.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Argentina", List.of(
                    new TaxType(null, TaxCategory.VAT, 27.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 29.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Colombia", List.of(
                    new TaxType(null, TaxCategory.VAT, 19.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 25.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Dominican Republic", List.of(
                    new TaxType(null, TaxCategory.VAT, 18.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 24.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Costa Rica", List.of(
                    new TaxType(null, TaxCategory.VAT, 13.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 18.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0)
            ),
            "Spain", List.of(
                    new TaxType(null, TaxCategory.VAT, 21.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0),
                    new TaxType(null, TaxCategory.ISR, 25.0, LocalDate.of(2024, 12, 9), TaxStatus.PENDING, null, 0.0, 0.0)
            )
            // Agregue los países restantes aquí
    );


    /*
    public List<TaxDTO> calculateTaxes(Long userId) {
        // Fetch user details and incomes
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        List<IncomeEntityResponseDTO> incomes = incomeService.getIncomes(userId);

        // Calculate total income
        double totalIncome = incomes.stream()
                .mapToDouble(IncomeEntityResponseDTO::amount)
                .sum();

        // Get tax rates for the user's country
        String country = user.getCountry();
        List<TaxType> taxRates = TAX_CONFIG.getOrDefault(country, List.of());

        List<TaxDTO> taxDetails = new ArrayList<>();

        for (TaxType tax : taxRates) {
            // Check the tax status
            if (tax.getStatus() == TaxStatus.PAID) {
                taxDetails.add(new TaxDTO(
                        tax.getCategory().name(),
                        totalIncome, // Use the original total income
                        tax.getPercentage(),
                        tax.getExpirationDate(),
                        "PAID",
                        "Paid",
                        0,
                        tax.getAmountPaid()
                ));
            } else {
                // Recalculate pending taxes
                double taxDue = totalIncome * (tax.getPercentage() / 100);
                taxDetails.add(new TaxDTO(
                        tax.getCategory().name(),
                        totalIncome,
                        tax.getPercentage(),
                        tax.getExpirationDate(),
                        "PENDING",
                        "Pay",
                        taxDue,
                        0
                ));
            }
        }

        return taxDetails;
    }

     */

    public List<TaxDTO> calculateTaxes(Long userId) {
        // Retrieve the user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        // Get total income using the new method
        double totalIncome = incomeService.getTotalIncome(userId);

        // Get tax rates for the user's country
        String country = user.getCountry();
        List<TaxType> taxRates = TAX_CONFIG.getOrDefault(country, List.of());

        List<TaxDTO> taxDetails = new ArrayList<>();

        for (TaxType tax : taxRates) {
            if (tax.getStatus() == TaxStatus.PAID) {
                taxDetails.add(new TaxDTO(
                        tax.getCategory().name(),
                        totalIncome,
                        tax.getPercentage(),
                        tax.getExpirationDate(),
                        "PAID",
                        "Paid",
                        0,
                        tax.getAmountPaid()
                ));
            } else {
                double taxDue = totalIncome * (tax.getPercentage() / 100);
                taxDetails.add(new TaxDTO(
                        tax.getCategory().name(),
                        totalIncome,
                        tax.getPercentage(),
                        tax.getExpirationDate(),
                        "PENDING",
                        "Pay",
                        taxDue,
                        0
                ));
            }
        }

        return taxDetails;
    }


    public void updateIncome(UserEntity user, double newAmount) {
        // Fetch the user's latest income (assuming they have one or more income records)
        List<Income> incomes = incomeRepository.findByUserId(user.getId());

        // Assuming you want to update the first income record (or the appropriate one based on your logic)
        Income income = incomes.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Income not found for user"));

        // Update the income amount
        income.setAmount(newAmount);

        // Persist the updated income entity
        incomeRepository.save(income);
    }


    public void updateTaxDetailsInDatabase(List<TaxDTO> taxes, Long userId) {
        for (TaxDTO tax : taxes) {
            // Convert the tax name (string) from DTO to the TaxCategory enum
            TaxCategory taxCategory;
            try {
                taxCategory = TaxCategory.valueOf(tax.getTaxName().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Invalid tax category: " + tax.getTaxName(), e);
            }

            // Fetch the TaxType entity using the updated repository method
            TaxType taxEntity = (TaxType) taxTypeRepository.findByUserIdAndCategory(userId, taxCategory)
                    .orElseThrow(() -> new IllegalStateException("Tax not found for category: " + tax.getTaxName()));

            // Update fields in the entity
            taxEntity.setStatus(TaxStatus.valueOf(tax.getStatus().toUpperCase())); // Ensure correct enum mapping
            taxEntity.setAmountPaid(tax.getAmountPaid());
            taxEntity.setTaxDue(tax.getTaxDue());

            // Save the updated entity
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

    /*
    public List<TaxDTO> getTaxesForUser(Long userId) {
        // Retrieve the user and incomes
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        List<IncomeEntityResponseDTO> incomes = incomeService.getIncomes(userId);

        // Calculate total income
        double totalIncome = incomes.stream()
                .mapToDouble(IncomeEntityResponseDTO::amount)
                .sum();

        // Retrieve taxes for the user
        List<TaxType> userTaxes = taxTypeRepository.findByUserId(userId);

        return userTaxes.stream()
                .map(tax -> {
                    // Keep paid taxes unchanged
                    if (tax.getStatus() == TaxStatus.PAID) {
                        return new TaxDTO(
                                tax.getCategory().name(),
                                totalIncome, // Use the original total income
                                tax.getPercentage(),
                                tax.getExpirationDate(),
                                "PAID",
                                "Paid",
                                0,
                                tax.getAmountPaid()
                        );
                    }

                    // Recalculate pending taxes
                    double taxDue = totalIncome * (tax.getPercentage() / 100);
                    return new TaxDTO(
                            tax.getCategory().name(),
                            totalIncome,
                            tax.getPercentage(),
                            tax.getExpirationDate(),
                            "PENDING",
                            "Pay",
                            taxDue,
                            0
                    );
                })
                .collect(Collectors.toList());
    }

     */

    public List<TaxDTO> getTaxesForUser(Long userId) {
        // Retrieve the user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        // Get total income using the new method
        double totalIncome = incomeService.getTotalIncome(userId);

        // Retrieve taxes for the user
        List<TaxType> userTaxes = taxTypeRepository.findByUserId(userId);

        return userTaxes.stream()
                .map(tax -> {
                    if (tax.getStatus() == TaxStatus.PAID) {
                        return new TaxDTO(
                                tax.getCategory().name(),
                                totalIncome,
                                tax.getPercentage(),
                                tax.getExpirationDate(),
                                "PAID",
                                "Paid",
                                0,
                                tax.getAmountPaid()
                        );
                    } else {
                        double taxDue = totalIncome * (tax.getPercentage() / 100);
                        return new TaxDTO(
                                tax.getCategory().name(),
                                totalIncome,
                                tax.getPercentage(),
                                tax.getExpirationDate(),
                                "PENDING",
                                "Pay",
                                taxDue,
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
                // Notify 4 days before deadline
                taxNotificationEmailService.sendTaxDeadlineNotification(user.getEmail(), user.getCountry(), tax.getExpirationDate());
            } else if (daysUntilDeadline > 0) {
                // Notify daily until the deadline if not paid
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

    /*
    private void paySpecificTax(Long userId, TaxCategory taxCategory) {
        // Fetch user details and incomes
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        List<IncomeEntityResponseDTO> incomes = incomeService.getIncomes(userId);

        // Calculate total income
        double totalIncome = incomes.stream()
                .mapToDouble(IncomeEntityResponseDTO::amount)
                .sum();

        // Retrieve taxes for the user
        List<TaxDTO> taxes = calculateTaxes(userId);

        // Find the selected tax based on the category
        TaxDTO paidTax = taxes.stream()
                .filter(tax -> tax.getTaxName().equalsIgnoreCase(taxCategory.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid tax category: " + taxCategory));

        // Check if the selected tax is already paid
        if (paidTax.getStatus().equalsIgnoreCase("PAID")) {
            throw new IllegalStateException("Tax " + taxCategory.name() + " is already paid.");
        }

        // Mark the selected tax as paid
        double taxDue = paidTax.getTaxDue();
        paidTax.setStatus("PAID");
        paidTax.setAmountPaid(taxDue);
        paidTax.setTaxDue(0);

        // Update the income amount by subtracting the paid tax
        double remainingIncome = totalIncome - taxDue;
        updateIncome(user, remainingIncome); // Only updates the income

        // Update the tax details in the database (only for the paid tax)
        updateTaxDetailsInDatabase(List.of(paidTax), userId);
    }

     */

    private void paySpecificTax(Long userId, TaxCategory taxCategory) {
        // Retrieve the user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        // Get total income using the new method
        double totalIncome = incomeService.getTotalIncome(userId);

        // Retrieve taxes for the user
        List<TaxDTO> taxes = calculateTaxes(userId);

        // Find the selected tax based on the category
        TaxDTO paidTax = taxes.stream()
                .filter(tax -> tax.getTaxName().equalsIgnoreCase(taxCategory.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid tax category: " + taxCategory));

        // Check if the selected tax is already paid
        if (paidTax.getStatus().equalsIgnoreCase("PAID")) {
            throw new IllegalStateException("Tax " + taxCategory.name() + " is already paid.");
        }

        // Mark the selected tax as paid
        double taxDue = paidTax.getTaxDue();
        paidTax.setStatus("PAID");
        paidTax.setAmountPaid(taxDue);
        paidTax.setTaxDue(0);

        // Update the income amount by subtracting the paid tax
        double remainingIncome = totalIncome - taxDue;
        updateIncome(user, remainingIncome);

        // Update the tax details in the database (only for the paid tax)
        updateTaxDetailsInDatabase(List.of(paidTax), userId);
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
    /*

    public double calculateTotalPayments(Long userId) {
        // Fetch all taxes for the user
        List<TaxDTO> taxes = getTaxesForUser(userId);

        // Calculate the sum of amountPaid for taxes with status "PAID"
        return taxes.stream()
                .filter(tax -> tax.getStatus().equalsIgnoreCase("PAID"))
                .mapToDouble(TaxDTO::getAmountPaid)
                .sum();
    }

     */


        /*

        public void sendDailyTaxNotificationsForUser(UserEntity user) {
            List<TaxType> pendingTaxes = taxTypeRepository.findByUserAndStatus(user, TaxStatus.PENDING);

            for (TaxType tax : pendingTaxes) {
                LocalDate today = LocalDate.now();
                long daysUntilDeadline = ChronoUnit.DAYS.between(today, tax.getExpirationDate());

                if (daysUntilDeadline == 4) {
                    // Notify 4 days before deadline
                    taxNotificationEmailService.sendTaxDeadlineNotification(user.getEmail(), user.getCountry(), tax.getExpirationDate());
                } else if (daysUntilDeadline > 0) {
                    // Notify daily until the deadline if not paid
                    taxNotificationEmailService.sendDailyPaymentReminder(user.getEmail(), tax);
                }
            }
        }

         */

}
