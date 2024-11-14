package com.igrowker.miniproject.User.Exception;
import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<UserProfileResponseDTO> handleInvalidUserId(InvalidUserIdException ex) {
        UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserProfileResponseDTO> handleUserNotFound(UserNotFoundException ex) {
        UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserProfileResponseDTO> handleGeneralException(Exception ex) {
        UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, null, "Hubo un error en el servidor");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}