package com.igrowker.miniproject.Payment;

import java.util.List;
import java.util.Optional;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Valid;

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
    public List<Payment> getPaymentsByCategory(@Valid CategoryPayment category) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentsByCategoryType'");
    }


    @Override
    public List<Payment> getPaymentsByYear(String year) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentsByYear'");
    }


    @Override
    public List<Payment> getPaymentsByMonth(String month) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentsByMonth'");
    }


    @Override
    public List<Payment> getPaymentsByYearAndMonth(String month) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentsByYearAndMonth'");
    }


    @Override
    public void createPayment() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPayment'");
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
