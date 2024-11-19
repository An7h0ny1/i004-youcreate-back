package com.igrowker.miniproject.Payment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igrowker.miniproject.Utils.Api_Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;
    
    @GetMapping("")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return (paymentService.getAllPayments().isEmpty()) ? ResponseEntity.ok(payments): ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Tag(name = "get a payment by id", description = "You can get a payment by id")
    @Operation(summary = "Get User Profile", description = "Get payment data by id.")
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "pago no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request error")
    })
    public ResponseEntity<Api_Response<Payment>> getPaymentById(@PathVariable @Valid Long id) throws Exception{
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(new Api_Response<>(payment, "pago encontrado con exito!", 200));
    }

    @GetMapping("/category/{category}")
    @Tag(name = "get payments by category", description = "You can get a payments by category")
    @Operation(summary = "Get payments category", description = "Get payments data by category.")
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "pagos no encontrados"),
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request error para las categorias")
    })
    public ResponseEntity<Api_Response<List<Payment>>>  getPaymentByCategory(@RequestParam CategoryPayment category) throws Exception {
        List<Payment> payments = paymentService.getPaymentsByCategory(category);
        return ResponseEntity.ok(new Api_Response<>(payments, "pagos encontrados con exito!", 200));
    }

    @GetMapping("/year/{year}")
    @Tag(name = "get payments by year", description = "You can get a payments by year")
    @Operation(summary = "Get payments year", description = "Get payments data by year.")
    public ResponseEntity<Api_Response<List<Payment>>> getPaymentsByYear(@RequestParam String year) throws Exception{
        List<Payment> payments = paymentService.getPaymentsByYear(year);
        return ResponseEntity.ok(new Api_Response<>(payments, "pagos encontrados con exito!", 200));
    }
    

    
    
    
}
