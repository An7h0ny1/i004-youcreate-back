package com.igrowker.miniproject.Config.Exceptions;

import com.igrowker.miniproject.Collaborator.Exception.BadCollaboratorBodyRequestException;
import com.igrowker.miniproject.Collaborator.Exception.CollaboratorNotFoundException;
import com.igrowker.miniproject.Collaborator.Exception.InvalidCollaboratorIdException;
import com.igrowker.miniproject.Income.Exception.BadIncomeBodyRequestException;
import com.igrowker.miniproject.Income.Exception.IncomeNotFoundException;
import com.igrowker.miniproject.Income.Exception.InvalidIncomeIdException;
import com.igrowker.miniproject.User.Exception.PasswordMismatchException;
import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import com.igrowker.miniproject.User.Exception.InvalidUserIdException;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.Utils.Api_Response;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid data", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Data integrity violation", ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Invalid request body", ex.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Resource not found", ex.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponse("Method not allowed", ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("Missing request parameter", ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(new ErrorResponse("Constraint violation", String.join(", ", errors)));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "User Not Found");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid Credentials");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Map<String, String>> handlePasswordMismatchException(PasswordMismatchException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Password Mismatch");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<UserProfileResponseDTO> handleInvalidUserId(InvalidUserIdException ex) {
        UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, null, null, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserProfileResponseDTO> handleUserNotFound(UserNotFoundException ex) {
        UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, null, null, null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidCollaboratorIdException.class, CollaboratorNotFoundException.class, BadCollaboratorBodyRequestException.class})
    public ResponseEntity<Api_Response<Object>> handleCollaboratorExceptions(Exception ex) {
        int statusCode;
        if (ex instanceof InvalidCollaboratorIdException || ex instanceof BadCollaboratorBodyRequestException) {
            statusCode = 400;
        } else if (ex instanceof CollaboratorNotFoundException) {
            statusCode = 404;
        } else {
            statusCode = 500;
        }
        return ResponseEntity.status(statusCode)
                .body(new Api_Response<>(null, ex.getMessage(), statusCode));
    }

    @ExceptionHandler({InvalidIncomeIdException.class, IncomeNotFoundException.class, BadIncomeBodyRequestException.class})
    public ResponseEntity<Api_Response<Object>> handleIncomeExceptions(Exception ex) {
        int statusCode;
        if (ex instanceof InvalidIncomeIdException || ex instanceof BadIncomeBodyRequestException) {
            statusCode = 400;
        } else if (ex instanceof IncomeNotFoundException) {
            statusCode = 404;
        } else {
            statusCode = 500;
        }
        return ResponseEntity.status(statusCode)
                .body(new Api_Response<>(null, ex.getMessage(), statusCode));
    }

}
