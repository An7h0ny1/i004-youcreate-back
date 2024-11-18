package com.igrowker.miniproject.User.Controller;

import com.igrowker.miniproject.User.Dto.UserProfileResponseDTO;
import com.igrowker.miniproject.User.Dto.UserUpdateRequestDTO;
import com.igrowker.miniproject.User.Exception.InvalidUserIdException;
import com.igrowker.miniproject.User.Exception.UserNotFoundException;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Service.UserService;
import com.igrowker.miniproject.Utils.Api_Response;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserService profilePhotoService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("")
    public void SaveUser(@RequestBody UserEntity entity) {
        
        userService.saveUser(entity);
    }
    


    @GetMapping("/{id}")
    @Tag(name = "UserProfile", description = "API for get user profile data.")
    @Operation(summary = "Get User Profile", description = "Get user profile data by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado satisfactoriamente"),
            @ApiResponse(responseCode = "400", description = "El id del usuario debe ser mayor a 0 o datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<UserProfileResponseDTO>> getUserProfile(@PathVariable @Valid Long id) {
        try {
            UserProfileResponseDTO response = userService.getUserProfile(id);
            return ResponseEntity.ok(new Api_Response<>(response, "Usuario encontrado satisfactoriamente", 200));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Api_Response<>(null, e.getMessage(), 404));
        } catch (InvalidUserIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Api_Response<>(null, e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Api_Response<>(null, "Hubo un error en el servidor", 500));
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

    @PutMapping("/{id}")
    @Tag(name = "UpdateDataUser", description = "API for update user profile data.")
    @Operation(summary = "Update User Fields", description = "Update specific fields of user profile by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado satisfactoriamente"),
            @ApiResponse(responseCode = "400", description = "El id del usuario debe ser mayor a 0 o datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Api_Response<UserProfileResponseDTO>> updateFields(@PathVariable @Valid Long id, @Valid @RequestBody UserUpdateRequestDTO request) {
        try {
            UserProfileResponseDTO response = userService.updateUserProfile(id, request);
            return ResponseEntity.ok(new Api_Response<>(response, "Usuario actualizado satisfactoriamente", 200));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Api_Response<>(null, e.getMessage(), 404));
        } catch (InvalidUserIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Api_Response<>(null, e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Api_Response<>(null, "Hubo un error en el servidor", 500));
        }
    }

    public ResponseEntity<UserProfileResponseDTO> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new UserProfileResponseDTO(null, null, null, null, null, null));
    }
}
