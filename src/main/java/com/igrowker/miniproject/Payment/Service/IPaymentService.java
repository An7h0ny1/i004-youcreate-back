package com.igrowker.miniproject.Payment.Service;

import java.util.List;

import com.igrowker.miniproject.Payment.DTO.PaymentDTO;
import com.igrowker.miniproject.Payment.Model.Payment;
import com.igrowker.miniproject.Payment.Model.PaymentStatus;

import jakarta.validation.Valid;

public interface IPaymentService {
    
    List<Payment> getAllPayments();
    Payment getPaymentById(Long id) throws Exception;
    List<Payment> getPaymentsByStatus(@Valid PaymentStatus Status) throws Exception;
    List<Payment> getPaymentsByYear(int year) throws Exception;
    List<Payment> getPaymentsByMonth(int month) throws Exception;
    List<Payment> getPaymentsByYearAndMonth(int year, int month) throws Exception;

    void createPayment(PaymentDTO payment) throws Exception;
    void editPayment(Long id, Payment payment) throws Exception;
    void partiallyEditPayment(Long id, Payment payment) throws Exception;
    void deletePaymentById(Long id) throws Exception;
}
