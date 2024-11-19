package com.igrowker.miniproject.Payment;

import java.util.List;

import jakarta.validation.Valid;

public interface IPaymentService {
    
    List<Payment> getAllPayments();
    Payment getPaymentById(Long id) throws Exception;
    List<Payment> getPaymentsByCategory(@Valid CategoryPayment category) throws Exception;
    List<Payment> getPaymentsByYear(String year) throws Exception;
    List<Payment> getPaymentsByMonth(String month) throws Exception;
    List<Payment> getPaymentsByYearAndMonth(String month) throws Exception;

    void createPayment() throws Exception;
    void editPayment(Payment payment) throws Exception;
    void deletePaymentById(Long payment) throws Exception;
}
