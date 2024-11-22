package com.igrowker.miniproject.Payment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.igrowker.miniproject.User.Exception.InvalidUserIdException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(Long id) throws Exception {
        verifyId(id);
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            return payment.get();
        }
        throw new PaymentNotFoundException("No se encontro el pago de id:" + id);
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
    public void createPayment(Payment payment) throws Exception {
        if (payment == null) 
            throw new NullPointerException("El pago no puede ser null");
        
        paymentRepository.save(payment);
    }

    @Override
    public void editPayment(Long id, Payment payment) throws Exception {
        Optional<Payment> paymentOPT = paymentRepository.findById(id);

        if (paymentOPT.isPresent()) {

            Payment paymentRegister = paymentOPT.get();

            paymentRegister.setAmount(payment.getAmount());
            paymentRegister.setCategory(payment.getCategory());
            paymentRegister.setService(payment.getService());
            paymentRegister.setStatus(payment.getStatus());
            paymentRegister.setDate(payment.getDate());
            paymentRegister.setCollaborator_id(payment.getCollaborator_id());

            paymentRepository.save(paymentRegister);
            return;
        }
        throw new PaymentNotFoundException("No se ha encontrado a ese pago en el sistema");
    }

    @Override
    public void partiallyEditPayment(Long id, Payment payment) throws Exception {
        Optional<Payment> paymentOPT = paymentRepository.findById(id);

        if (paymentOPT.isPresent()) {

            Payment paymentRegister = paymentOPT.get();

            if (payment.getService() != null)
                paymentRegister.setService(payment.getService());

            if (payment.getAmount() != null)
                paymentRegister.setAmount(payment.getAmount());

            if (payment.getStatus() != null)
                paymentRegister.setStatus(payment.getStatus());

            if (payment.getCategory() != null)
                paymentRegister.setCategory(payment.getCategory());

            if (payment.getDate() != null)
                paymentRegister.setDate(payment.getDate());

            if (payment.getCollaborator_id() != null)
                paymentRegister.setCollaborator_id(payment.getCollaborator_id());

            paymentRepository.save(paymentRegister);
            return;
        }
        throw new PaymentNotFoundException("No se ha encontrado a ese pago en el sistema");
    }

    @Override
    public void deletePaymentById(Long id) throws Exception {
        verifyId(id);
        paymentRepository.deleteById(id);
    }

    private void verifyId(Long id){
        if(id < 0) throw new InvalidUserIdException("El id debe ser mayor o igual a 0");
    }
    private void  verifyYear(int year) throws IllegalArgumentException{
        if (year < 2000 || year > 2100) {
            throw new IllegalArgumentException("El año debe estar entre 2000 y 2100");
        }
    }

    private void verifyMonth(int month){
        if (month < 1 || month > 12){
            throw new IllegalArgumentException("El mes debe estar entre 1  y 12");
        }
    }
}
