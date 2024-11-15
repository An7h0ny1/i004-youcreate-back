package com.igrowker.miniproject.User.Controller;

import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import com.igrowker.miniproject.User.Dto.UserUpdateRequestDTO;
import com.igrowker.miniproject.User.Model.UserEntity;
//import com.igrowker.miniproject.User.Model.Enum.EnumCountry;
import com.igrowker.miniproject.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Tag(name = "UserProfile", description = "API for get user profile data.")
    @Operation(summary = "Get User Profile", description = "Get user profile data by id.")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            if (id <= 0) {
                UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, null, "El id del usuario debe ser mayor a 0");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            UserEntity userEntity = userService.getUserProfile(id);
            if (userEntity == null) {
                UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, null, "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            String country = userEntity.getCountry();
            UserProfileResponseDTO response = new UserProfileResponseDTO(userEntity.getId(), country, userEntity.getUserName(), userEntity.getEmail(), "Usuario encontrado satisfactoriamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            UserProfileResponseDTO response = new UserProfileResponseDTO(null, null, null, null, "Hubo un error en el servidor");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    @Tag(name = "UpdateDataUser", description = "API for update user profile data.")
    @Operation(summary = "Update User Fields", description = "Update specific fields of user profile by id.")
    public ResponseEntity<UserProfileResponseDTO> updateFields(@PathVariable Long id, @RequestBody UserUpdateRequestDTO request) {
        try {
            if (id <= 0) {
                return buildErrorResponse("El id del usuario debe ser mayor a 0", HttpStatus.BAD_REQUEST);
            }

            UserEntity userEntity = userService.getUserProfile(id);
            if (userEntity == null) {
                return buildErrorResponse("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }

            userEntity.setUserName(request.getUserName() + " " + request.getLastName());
            userEntity.setEmail(request.getEmail());
            userService.saveUser(userEntity);

            return ResponseEntity.ok(new UserProfileResponseDTO(userEntity.getId(), userEntity.getCountry(), userEntity.getUserName(), userEntity.getEmail(), "Usuario actualizado satisfactoriamente"));
        } catch (Exception e) {
            return buildErrorResponse("Hubo un error en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private ResponseEntity<UserProfileResponseDTO> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new UserProfileResponseDTO(null, null, null, null, message));
    }
}
