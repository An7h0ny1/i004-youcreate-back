package com.igrowker.miniproject.Payment;

import java.util.List;

import jakarta.validation.Valid;

public interface IPaymentService {
    
    List<Payment> getAllPayments();
    Payment getPaymentById(Long id) throws Exception;
    List<Payment> getPaymentsByStatus(@Valid PaymentStatus Status) throws Exception;
    List<Payment> getPaymentsByYear(int year) throws Exception;
    List<Payment> getPaymentsByMonth(int month) throws Exception;
    List<Payment> getPaymentsByYearAndMonth(int year, int month) throws Exception;

    void createPayment(Payment payment) throws Exception;
    void editPayment(Payment payment) throws Exception;
    void deletePaymentById(Long payment) throws Exception;
}
