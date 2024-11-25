package com.igrowker.miniproject.Payment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.igrowker.miniproject.Payment.Model.Payment;
import com.igrowker.miniproject.Payment.Model.PaymentStatus;
import com.igrowker.miniproject.Payment.Repository.PaymentRepository;

@SpringBootTest
public class PaymentRepositoryTest {

    @Mock
    private PaymentRepository paymentRepository;

    @BeforeEach
    public void setUp(){   
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        reset(paymentRepository);
    }

    @Test
    @DisplayName("Deberia crear un pago correctamente")
    public void savePayment(){
       
        Payment payment = Mockito.any(Payment.class);
        paymentRepository.save(payment);
        Mockito.verify(paymentRepository, Mockito.times(1)).save(payment);
    }

    @Test
    @DisplayName("Deberia borrar un pago correctamente e invocar solo 1 vez a delete by id")
    public void deletePayment(){
        Payment payment = new Payment();
        payment.setId(1L);
        Mockito.when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        paymentRepository.deleteById(paymentRepository.findById(1L).get().getId());
        Mockito.verify(paymentRepository, Mockito.times(1)).deleteById(1L);
    }

    
    @SuppressWarnings("null")
    @Test
    @DisplayName("Deberia lanzar una excepcion de punto nulo ya que el id es invalido")
    public void deletePaymentButPaymentIsNull(){
        Long id = null;

        Mockito.doThrow(IllegalArgumentException.class).when(paymentRepository).deleteById(id);

        assertThrows(IllegalArgumentException.class, () -> paymentRepository.deleteById(id));
    }

    @Test
    @DisplayName("Deberia buscar un elemento por id sin lanzar excepcion")
    public void findById(){
        paymentRepository.findById(Mockito.anyLong());
        Mockito.verify(paymentRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Deberia buscar un elemento por año sin lanzar excepcion")
    public void findByYear(){
        paymentRepository.findByYear(Mockito.anyInt());
        Mockito.verify(paymentRepository, Mockito.times(1)).findByYear(Mockito.anyInt());
    }

    @Test
    @DisplayName("Deberia buscar un elemento por mes sin lanzar excepcion")
    public void findByMonth(){
        paymentRepository.findByMonth(Mockito.anyInt());
        Mockito.verify(paymentRepository, Mockito.times(1)).findByMonth(Mockito.anyInt());
    }

    @Test
    @DisplayName("Deberia buscar un elemento por año y mes sin lanzar excepcion")
    public void findByMonthAndYear(){
        paymentRepository.findByMonthAndYear(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(paymentRepository, Mockito.times(1)).findByMonthAndYear(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    @DisplayName("Deberia buscar un elemento por estado del pago sin lanzar excepcion")
    public void findByStatus(){
        PaymentStatus paymentStatus = Mockito.any(PaymentStatus.class);
        paymentRepository.findByStatus(paymentStatus);
        Mockito.verify(paymentRepository, Mockito.times(1)).findByStatus(paymentStatus);
    }

    @Test
    @DisplayName("No deberia lanzar nada con estado en null")
    public void findByStatusNull(){
       
        paymentRepository.findByStatus(null);
        Mockito.verify(paymentRepository, Mockito.times(1)).findByStatus(null);
    }
}