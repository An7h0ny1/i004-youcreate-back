package com.igrowker.miniproject.Payment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    
    @Override
    public Payment getPaymentById(Long id) throws Exception{
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()){
            return payment.get();
        }
        throw new PaymentNotFoundException("No se encontro el pago de id:" + id);
    }




    @Override
    public List<Payment> getPaymentsByYear(int year) throws Exception {
        List<Payment> payments = paymentRepository.findByYear(year);
        return payments;
    }

    @Override
    public List<Payment> getPaymentsByMonth(int month) throws Exception {
        List<Payment> payments = paymentRepository.findByMonth(month);
        return payments;
    }


    @Override
    public List<Payment> getPaymentsByStatus(@Valid PaymentStatus Status) throws Exception {
        List<Payment> payments = paymentRepository.findByStatus(Status);
        return payments;
    }


    @Override
    public List<Payment> getPaymentsByYearAndMonth(int year, int month) throws Exception {
        List<Payment> payments = paymentRepository.findByMonthAndYear(month, year);
        return payments;
    }


    @Override
    public void createPayment(Payment payment) throws Exception {
        paymentRepository.save(payment);
    }


    @Override
    public void editPayment(Payment payment) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editPayment'");
    }


    @Override
    public void deletePaymentById(Long payment) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePaymentById'");
    }


 

    


    
    
}
