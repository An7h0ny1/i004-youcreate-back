package com.igrowker.miniproject.User.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

import com.igrowker.miniproject.User.Exception.PasswordMismatchException;
import com.igrowker.miniproject.User.Model.PasswordReset;
import com.igrowker.miniproject.User.Service.PasswordResetService;

import lombok.Data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/reset")
@Tag(name = "Password Reset", description = "Password Reset API")
public class PasswordResetController {

    @Autowired
    PasswordResetService senderResetService;

    @GetMapping("")
    public ResponseEntity<?> getAllPasswordsReset() {
        try {
            List<PasswordReset> resets = senderResetService.getAllPasswordsReset();
            return ResponseEntity.ok(resets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    

    @PostMapping("/{email}")
    @Operation(
            summary = "Send Email Reset",
            description = "Send Email Reset",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Send Email Reset")
            }
    )
    public ResponseEntity<String> sendResetEmail(@PathVariable String email) {

        try {
            senderResetService.sendEmailReset(email);
            return ResponseEntity.ok("Se ha enviado el mail a:" + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PatchMapping("/{token}")
    @Operation(
            summary = "Reset Password",
            description = "Reset Password",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reset Password")
            }
    )
    public ResponseEntity<?> resetPassword(@org.springframework.web.bind.annotation.RequestBody UserReset user, @PathVariable String token)  {
        try {
            boolean status = senderResetService.validate(token, user.getEmail(), user.getPassword());
            if (!status) return ResponseEntity.ok("el token es invalido");
            return ResponseEntity.ok("Se ha actualizado correctamente");
        } catch(IllegalStateException exception){
            return ResponseEntity.ok(exception.getMessage());
        }  catch(PasswordMismatchException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
       

    }

}

@Data
class UserReset {
    String email;
    String password;
}


