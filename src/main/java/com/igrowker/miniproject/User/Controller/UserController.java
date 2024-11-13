package com.igrowker.miniproject.User.Controller;

import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            if (id <= 0) {
                UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, "Id de usuario no válido");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            UserEntity userEntity = userService.getUserProfile(id);
            if (userEntity == null) {
                UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            UserProfileResponseDTO response = new UserProfileResponseDTO(userEntity.getId(), userEntity.getUserName(), userEntity.getEmail(), "Usuario encontrado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, "Hubo un error en el servidor");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
