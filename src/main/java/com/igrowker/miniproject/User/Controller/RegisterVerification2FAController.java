package com.igrowker.miniproject.User.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igrowker.miniproject.User.Dto.TwoFARegisterVerificationDTO;
import com.igrowker.miniproject.User.Model.RegisterVerification2FA;
import com.igrowker.miniproject.User.Service.RegisterVerification2FAService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/verifyRegister")
public class RegisterVerification2FAController {

    @Autowired
    private RegisterVerification2FAService registerVerification2FAService;

    @GetMapping("")
    public List<RegisterVerification2FA> getAllRegisters() throws Exception {
        return registerVerification2FAService.getAllRegisters();
    }

    @PostMapping("/{email}")
    public ResponseEntity<?> sendEmailfor2FAVerification(@PathVariable String email) {

        Optional<RegisterVerification2FA> register = registerVerification2FAService.validateEmail(email);

        if (register.isPresent()) {
            return ResponseEntity.status(402).build();
        }
        try {
            registerVerification2FAService.sendEmailForVerification2FA(email);
            return ResponseEntity.ok("Se ha enviado el mail correctamente a:" + email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/verificar-2fa")
    public ResponseEntity<?> verificarCodigo2FA(@RequestBody TwoFARegisterVerificationDTO body) {

        try {
            registerVerification2FAService.verificate2FAtoken(body);
            return ResponseEntity.ok("verificacion exitosa!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}