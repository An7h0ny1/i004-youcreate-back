package com.igrowker.miniproject.Payment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.igrowker.miniproject.Payment.Model.Payment;
import com.igrowker.miniproject.Payment.Model.PaymentMethod;
import com.igrowker.miniproject.Payment.Model.PaymentStatus;
import com.igrowker.miniproject.Payment.Repository.PaymentRepository;
import com.igrowker.miniproject.Payment.Service.PaymentService;

@SpringBootTest
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;
    Payment payment; 
    @BeforeEach
    public void setUp(){
        payment = new Payment();
        
        payment.setAmount(30.0);
        payment.setCollaborator_id(Long.valueOf(3));
        payment.setService("Creacion de UX");
        payment.setStatus(PaymentStatus.PAID);
        payment.setCategory(PaymentMethod.BANK_TRANSFER);
        
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deberia crear un pago correctamente")
    public void createPayment(){
        //assertDoesNotThrow(() ->  paymentService.createPayment(payment));
        Mockito.verify(paymentRepository, Mockito.times(1)).save(payment);
    }

    @Test
    @DisplayName("Deberia lanzar un error al crear un pago por que es null")
    public void createPaymentButPaymentIsNull() throws Exception {
        assertThrows(NullPointerException.class, () -> paymentService.createPayment(null));
        Mockito.verify(paymentRepository, Mockito.times(0)).save(null);
    }

    
}