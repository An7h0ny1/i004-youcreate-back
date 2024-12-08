package com.igrowker.miniproject.Payment.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.igrowker.miniproject.Collaborator.DTO.CollaboratorEntityResponseDTO;
import com.igrowker.miniproject.Collaborator.Service.CollaboratorService;
import com.igrowker.miniproject.Payment.DTO.PaymentDTO;
import com.igrowker.miniproject.Payment.Exception.PaymentNotFoundException;
import com.igrowker.miniproject.Payment.Model.Payment;
import com.igrowker.miniproject.Payment.Model.PaymentStatus;
import com.igrowker.miniproject.Payment.Repository.PaymentRepository;
import com.igrowker.miniproject.User.Exception.InvalidUserIdException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@EnableScheduling
public class PaymentService implements IPaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private CollaboratorService collaboratorService;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(Long id) throws Exception {
        verifyId(id);
        Payment payment = paymentRepository
                .findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("No se encontro el pago de id:" + id));

        return payment;
    }

    @Override
    public List<Payment> getPaymentsByYear(int year) throws Exception {
        verifyYear(year);
        List<Payment> payments = paymentRepository.findByYear(year);
        return payments;
    }

    @Override
    public List<Payment> getPaymentsByMonth(int month) throws Exception {
        verifyMonth(month);
        List<Payment> payments = paymentRepository.findByMonth(month);
        return payments;
    }

    @Override
    public List<Payment> getPaymentsByStatus(PaymentStatus Status) throws Exception {
        List<Payment> payments = paymentRepository.findByStatus(Status);
        return payments;
    }

    @Override
    public List<Payment> getPaymentsByYearAndMonth(int year, int month) throws Exception {
        verifyYear(year);
        verifyMonth(month);
        List<Payment> payments = paymentRepository.findByMonthAndYear(month, year);
        return payments;
    }

    @Override
    public List<Payment> getPaymentsForReminder(int days) {
        List<Payment> payments = paymentRepository.findByStatus(PaymentStatus.PENDING);
        return payments.stream().filter(new Predicate<Payment>() {

            @Override
            public boolean test(Payment payment) {

                if (payment.getStatus().equals(PaymentStatus.EXPIRED))
                    return false;

                LocalDate reminderDate = payment.getExpired_date().minusDays(days);
                LocalDate today = LocalDate.now();

                return reminderDate.isEqual(today);
            }

        }).toList();
    }

    @Override
    public void createPayment(PaymentDTO paymentR) throws Exception {
        if (paymentR == null)
            throw new NullPointerException("El pago no puede ser null");

        CollaboratorEntityResponseDTO collaborator = collaboratorService.getCollaborator(paymentR.getCollaborator_id());

        Payment payment = PaymentService.paymentFromDTO(paymentR, collaborator);
        paymentRepository.save(payment);
    }

    @Override
    public void editPayment(Long id, Payment payment) throws Exception {
        Payment paymentRegister = paymentRepository
                .findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("No se ha encontrado a ese pago en el sistema"));

        paymentRegister.setAmount(payment.getAmount());
        paymentRegister.setCategory(payment.getCategory());
        paymentRegister.setService(payment.getService());
        paymentRegister.setStatus(payment.getStatus());
        paymentRegister.setDate(payment.getDate());
        paymentRegister.setCollaboratorId(payment.getCollaboratorId());

        paymentRepository.save(paymentRegister);
        return;

    }

    @Override
    public void partiallyEditPayment(Long id, Payment payment) throws Exception {
        Optional<Payment> paymentOPT = paymentRepository.findById(id);

        if (paymentOPT.isPresent()) {

            Payment paymentRegister = paymentOPT.get();

            updateField(payment.getService(), paymentRegister::setService);
            updateField(payment.getAmount(), paymentRegister::setAmount);
            updateField(payment.getStatus(), paymentRegister::setStatus);
            updateField(payment.getCategory(), paymentRegister::setCategory);
            updateField(payment.getDate(), paymentRegister::setDate);
            updateField(payment.getCollaboratorId(), paymentRegister::setCollaboratorId);

            paymentRepository.save(paymentRegister);
            return;
        }
        throw new PaymentNotFoundException("No se ha encontrado a ese pago en el sistema");
    }

    
    @Override
    public Payment pay(Long id) throws Exception {
        Payment payment = paymentRepository
        .findById(id)
        .orElseThrow(() -> new PaymentNotFoundException("No se ha encontrado el pago de id" + id));

        if (payment.getStatus().equals(PaymentStatus.EXPIRED)) 
            throw new IllegalStateException("El pago ya esta expirado");
        
        if(payment.getStatus().equals(PaymentStatus.PAID))
            throw new IllegalStateException("El pago ya esta pagado");

        updateField(PaymentStatus.PAID, payment::setStatus);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public void deletePaymentById(Long id) throws Exception {
        verifyId(id);
        paymentRepository.deleteById(id);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Se ejecuta todos los días a las 12:00 AM
    public void updatePaymentsStatus() {

        List<Payment> payments = paymentRepository.findByStatus(PaymentStatus.PENDING);

        payments.stream().filter(new Predicate<Payment>() {

            @Override
            public boolean test(Payment p) {
                return p.getExpired_date().isBefore(LocalDate.now());
            }

        }).toList().forEach((p) -> {
            p.setStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(p);
        });
        logger.info("Tarea completada: Estados de pagos actualizados.");
    }

    private void verifyId(Long id) {
        if (id < 0)
            throw new InvalidUserIdException("El id debe ser mayor o igual a 0");
    }

    private void verifyYear(int year) throws IllegalArgumentException {
        if (year < 2000 || year > 2100) {
            throw new IllegalArgumentException("El año debe estar entre 2000 y 2100");
        }
    }

    private void verifyMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1  y 12");
        }
    }

    private <T> void updateField(T value, Consumer<T> function) {

        if (value != null) {
            function.accept(value);
        }
    }

    private static Payment paymentFromDTO(PaymentDTO dto, CollaboratorEntityResponseDTO collaborator) {

        Payment payment = new Payment();
        payment.setAmount(collaborator.amount());
        payment.setCollaboratorId(collaborator.id());
        payment.setService(collaborator.service());
        payment.setDate(LocalDate.now());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCategory(dto.getCategory());
        payment.setExpired_date(LocalDate.now().plus(30, ChronoUnit.DAYS));
        return payment;
    }

    @Override
    public List<Payment> getPaymentsByIdCollaborator(Long id_collaborator) throws Exception {

        List<Payment> payments = paymentRepository.findByCollaboratorId(id_collaborator);

        if (payments.isEmpty())
            return null;

        return payments;
    }


}
