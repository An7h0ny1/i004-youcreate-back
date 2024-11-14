package com.igrowker.miniproject.PasswordRecovery.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.igrowker.miniproject.PasswordRecovery.Service.PasswordRecoverySenderService;
import com.igrowker.miniproject.User.Model.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/recovery")
public class PasswordRecoveryController {
    
    @Autowired
    PasswordRecoverySenderService SenderRecoveryService;
    
    @PostMapping("/{email}")
    public ResponseEntity<String> sendEmail(@PathVariable String email) {
        
        try {
            SenderRecoveryService.sendEmailVerification(email);
            return ResponseEntity.ok("Se ha enviado el mail a:" + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
         
    }
    
    
}
