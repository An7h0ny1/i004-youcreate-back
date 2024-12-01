package com.igrowker.miniproject.Payment.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.igrowker.miniproject.User.Exception.InvalidUserIdException;
import com.igrowker.miniproject.Utils.Api_Response;

@ControllerAdvice
public class PaymentException {
    
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Api_Response<?>> handleNotFoundPaymentException(PaymentNotFoundException e){

        return ResponseEntity.status(404).body(new Api_Response<>(null, e.getMessage(), 404));
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<Api_Response<Object>> handleInvalidPaymentIdException(InvalidUserIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Api_Response<>(null, ex.getMessage(), 400));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Api_Response<Object>> handleGenericPaymentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Api_Response<>(null, "Hubo un error en el servidor", 500));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Api_Response<?>> handleGenericIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new Api_Response<>(null, e.getMessage(), 400));

    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Api_Response<?>> handleGenericNullPointerException(NullPointerException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new Api_Response<>(null, e.getMessage(), 400));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Api_Response<?>> handleGenericMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new Api_Response<>(null, e.getMessage(), 400));

    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Api_Response<?>> handleGenericMethodIllegalStateException(IllegalStateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new Api_Response<>(null, e.getMessage(), 400));

    }
}
