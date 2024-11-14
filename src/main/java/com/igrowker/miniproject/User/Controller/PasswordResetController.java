package com.igrowker.miniproject.User.Controller;

import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> sendResetEmail(@PathVariable String email) {

        try {
            senderResetService.sendEmailReset(email);
            return ResponseEntity.ok("Se ha enviado el mail a:" + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PatchMapping("/{token}")
    public ResponseEntity<String> resetPassword(@org.springframework.web.bind.annotation.RequestBody UserReset user, @PathVariable String token) throws Exception {

        boolean status = senderResetService.validate(token, user.getEmail(), user.getPassword());
        return (status) ? ResponseEntity.ok("Se ha actualizado correctamente") : ResponseEntity.badRequest().build();

    }

}

@Data
class UserReset{
    String email;
    String password;
}


