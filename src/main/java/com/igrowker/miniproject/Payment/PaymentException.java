package com.igrowker.miniproject.Payment;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.igrowker.miniproject.User.Exception.InvalidUserIdException;
import com.igrowker.miniproject.Utils.Api_Response;

@RestControllerAdvice
public class PaymentException {
    
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Api_Response<?>> handleNotFoundPaymentException(PaymentNotFoundException e){

        return ResponseEntity.status(404).body(new Api_Response<>(null, e.getMessage(), 404));
    }

     @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Api_Response<Object>> handleInvalidPaymentIdException(InvalidUserIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Api_Response<>(null, ex.getMessage(), 400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Api_Response<Object>> handleGenericPaymentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Api_Response<>(null, "Hubo un error en el servidor", 500));
    }
}
