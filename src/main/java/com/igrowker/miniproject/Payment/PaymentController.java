package com.igrowker.miniproject.Payment;

import org.springframework.web.bind.annotation.RequestMapping;
import com.igrowker.miniproject.Utils.Api_Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;
    
    @GetMapping("")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Get all payments", description = "Get all payments data.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "no hay pagos registrados"),
        @ApiResponse(responseCode = "200", description = "ok! Se devuelven los pagos")

    })
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return (paymentService.getAllPayments().isEmpty()) ? ResponseEntity.ok(payments): ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Get payment by id", description = "Get payment data by id.")
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "pago no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request error")
    })
    public ResponseEntity<Api_Response<Payment>> getPaymentById(@PathVariable @Valid Long id) throws Exception{
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(new Api_Response<>(payment, "pago encontrado con exito!", 200));
    }

    @GetMapping("/status/{status}")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Get payments status", description = "Get payments data by status.")
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "pagos no encontrados"),
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request error en obtener los estados")
    })
    public ResponseEntity<Api_Response<List<Payment>>>  getPaymentBystatus(@RequestParam @Valid PaymentStatus status) throws Exception {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(new Api_Response<>(payments, "pagos encontrados con exito!", 200));
    }

    @GetMapping("/year/{year}")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Get payments by year", description = "Get payments data by year.")
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "pagos no encontrados"),
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request error segun año")
    })
    public ResponseEntity<Api_Response<List<Payment>>> getPaymentsByYear(@RequestParam int year) throws Exception{
        List<Payment> payments = paymentService.getPaymentsByYear(year);
        return ResponseEntity.ok(new Api_Response<>(payments, "pagos encontrados con exito!", 200));
    }

    @GetMapping("/month/{month}")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Get payments by month", description = "Get payments data by month.")
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "pagos no encontrados"),
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request error segun mes")
    })
    public ResponseEntity<Api_Response<List<Payment>>> getPaymentsByMonth(@RequestParam int month) throws Exception{
        List<Payment> payments = paymentService.getPaymentsByMonth(month);
        return ResponseEntity.ok(new Api_Response<>(payments, "pagos encontrados con exito!", 200));
    }

    @GetMapping("/year/{year}/month/{month}")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Get payments by year and month", description = "Get payments data by year and month.")
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "pagos no encontrados"),
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request error segun año y mes")
    })
    public ResponseEntity<Api_Response<List<Payment>>> getPaymentsByYearAndMonth(@RequestParam int year, @RequestParam int month) throws Exception{
        List<Payment> payments = paymentService.getPaymentsByYearAndMonth(year, month);
        return ResponseEntity.ok(new Api_Response<>(payments, "pagos encontrados con exito!", 200));
    }

    @PostMapping("")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Create a new payment", description = "You can create a new payment")
    @ApiResponses({
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request, el pago es invalido"),
        @ApiResponse(responseCode = "200", description = "ok! Se crea el nuevo pago")
    })
    public  ResponseEntity<?>  CreatePayment(@RequestBody @Valid Payment payment) throws Exception {
        paymentService.createPayment(payment);
        return ResponseEntity.ok(payment);
    }

    @PutMapping("{id}")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Edit a payment", description = "You can edit a payment")
    @ApiResponses({
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request, el pago es invalido"),
        @ApiResponse(responseCode = "200", description = "ok! Se edito el pago correctamente"),
        @ApiResponse(responseCode = "404", description = "pago no encontrado"),
    })
    public ResponseEntity<?> editPayment(@PathVariable Long id, @RequestBody @Valid Payment payment) throws Exception{
        paymentService.editPayment(id, payment);
        return ResponseEntity.ok(payment);
    }

    @DeleteMapping("{id}")
    @Tag(name = "Payment", description = "API for get payments")
    @Operation(summary = "Delete a payment", description = "You can delete a payment")
    @ApiResponses({
        @ApiResponse(responseCode = "500", description = "Error interno en el servidor"),
        @ApiResponse(responseCode = "400", description = "bad request, el pago es invalido"),
        @ApiResponse(responseCode = "200", description = "ok! Se elimino el pago correctamente"),
        @ApiResponse(responseCode = "404", description = "pago no encontrado"),
    })
    public ResponseEntity<?> deletePaymnet(@PathVariable Long id) throws Exception{
        paymentService.deletePaymentById(id);
        return ResponseEntity.ok("Eliminacion correcta!");
    }
    

    
    
    
}
