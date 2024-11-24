package com.igrowker.miniproject.Payment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.igrowker.miniproject.Collaborator.DTO.CollaboratorEntityResponseDTO;
import com.igrowker.miniproject.Collaborator.Model.Collaborator;
import com.igrowker.miniproject.Collaborator.Service.CollaboratorService;
import com.igrowker.miniproject.Payment.DTO.PaymentDTO;
import com.igrowker.miniproject.Payment.Exception.PaymentNotFoundException;
import com.igrowker.miniproject.Payment.Model.Payment;
import com.igrowker.miniproject.Payment.Model.PaymentMethod;
import com.igrowker.miniproject.Payment.Model.PaymentStatus;
import com.igrowker.miniproject.Payment.Repository.PaymentRepository;
import com.igrowker.miniproject.Payment.Service.PaymentService;
import com.igrowker.miniproject.User.Exception.InvalidUserIdException;

@SpringBootTest
public class PaymentServiceTest {

    @Mock
    @Autowired
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;
    private Payment payment; 

    @Mock
    private CollaboratorService collaboratorService;

    @BeforeEach
    public void setUp(){
        payment = new Payment();
        
        payment.setAmount(30.0);
        payment.setCollaborator_id(Long.valueOf(3));
        payment.setService("Creacion de UX");
        payment.setStatus(PaymentStatus.PAID);
        payment.setCategory(PaymentMethod.BANK_TRANSFER);
        payment.setDate(Instant.now());
        payment.setExpired_date(Instant.now().plus(30, ChronoUnit.DAYS));
        
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deberia crear un pago correctamente")
    public void createPayment(){
        paymentRepository.save(payment);
        Mockito.verify(paymentRepository, Mockito.times(1)).save(payment);
    }

    @Test
    @DisplayName("Deberia lanzar un error al crear un pago por que es null")
    public void createPaymentButPaymentIsNull() throws Exception {
        assertThrows(NullPointerException.class, () -> paymentService.createPayment(null));
        Mockito.verify(paymentRepository, Mockito.times(0)).save(null);
    }

    @Test
    @DisplayName("Deberia editar un pago correctamente")
    public void editPayment(){
        Payment payment = Mockito.mock(Payment.class);
        Mockito.when(payment.getAmount()).thenReturn(500.0);
        paymentRepository.save(payment);
        Mockito.verify(paymentRepository, Mockito.times(1)).save(payment);

        Optional<Payment> opt = Optional.ofNullable(payment);
        Mockito.when(paymentRepository.findById(payment.getId())).thenReturn(opt);
        assertEquals(paymentRepository.findById(payment.getId()).get().getAmount(), 500.0);
    }

    @Test
    @DisplayName("Deberia eliminar un pago correctamente")
    public void deletePayment(){
        Payment payment = Mockito.mock(Payment.class);
        Mockito.when(payment.getId()).thenReturn(Long.valueOf(-10));
        assertThrows(InvalidUserIdException.class, () -> paymentService.deletePaymentById(payment.getId()));
    }

    @Test
    @DisplayName("Deberia crear un pago de forma erronea")
    public void createPaymentErrorBecauseCollaboratorIsNull(){

        PaymentDTO payment = Mockito.mock(PaymentDTO.class);
    
        Mockito.when(payment.getCollaborator_id()).thenReturn(1L);

        Mockito.when(collaboratorService.getCollaborator(payment.getCollaborator_id())).thenReturn(null);
        assertThrows(NullPointerException.class, () ->  paymentService.createPayment(payment));

    }

    @Test
    @DisplayName("Deberia lanzar una excepcion ya que el pago no existe")
    public void PaymentEditPartiallyIsInvalid(){
        assertThrows(PaymentNotFoundException.class, () ->  paymentService.partiallyEditPayment(1L, payment));
    }

    
}