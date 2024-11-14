package com.igrowker.miniproject.User.Controller;

import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserService profilePhotoService;

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

    @Operation(summary = "Upload a profile photo")
    @PostMapping(value = "/update-photo", consumes = "multipart/form-data")
    public ResponseEntity<String> updateProfilePhoto(
            @Parameter(description = "Profile photo file", required = true)
            @RequestParam("photo") MultipartFile photo) {
        try {
            // Llama al servicio para guardar la foto.
            String filePath = profilePhotoService.saveProfilePhoto(photo);

            // Devuelve la ruta donde se guardó la foto (esto podría cambiarse para devolver una URL)
            return ResponseEntity.ok("Profile photo updated. Saved at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload photo: " + e.getMessage());
        }
    }
}
