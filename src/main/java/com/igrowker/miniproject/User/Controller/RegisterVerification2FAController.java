package com.igrowker.miniproject.User.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.igrowker.miniproject.User.Dto.TwoFARegisterVerificationDTO;
import com.igrowker.miniproject.User.Model.RegisterVerification2FA;
import com.igrowker.miniproject.User.Service.IRegisterVerification2FAService;
import com.igrowker.miniproject.User.Service.RegisterVerification2FAService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/verifyRegister")
@Tag(name = "Register Verification 2FA", description = "Register Verification 2FA API")
public class RegisterVerification2FAController {

    @Autowired
    private IRegisterVerification2FAService registerVerification2FAService;

    @GetMapping("")
    public List<RegisterVerification2FA> getAllRegisters() throws Exception {
        return registerVerification2FAService.getAllRegisters();
    }

    @PostMapping("/{email}")
    @Operation(
            summary = "Send Email for 2FA Verification",
            description = "Send Email for 2FA Verification",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Send Email for 2FA Verification")
            }
    )
    public ResponseEntity<?> sendEmailfor2FAVerification(@PathVariable String email) {

        Optional<RegisterVerification2FA> register = registerVerification2FAService.validateEmail(email);

        if (register.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya esta en el sistema!");
        }
        try {
            registerVerification2FAService.sendEmailForVerification2FA(email);
            return ResponseEntity.ok("Se ha enviado el mail correctamente a:" + email);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/verificar-2fa")
    @Operation(
            summary = "Verificar Codigo 2FA",
            description = "Verificar Codigo 2FA",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Verificar Codigo 2FA")
            }
    )
    public ResponseEntity<?> verificarCodigo2FA(@org.springframework.web.bind.annotation.RequestBody TwoFARegisterVerificationDTO body) {

        try {
            String result = registerVerification2FAService.verificate2FAtoken(body);
            if(result.equals("OK"))  return ResponseEntity.ok("verificacion exitosa! Completando registro");  
            return ResponseEntity.ok("OK, pero el token status es: " + result);
           
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}