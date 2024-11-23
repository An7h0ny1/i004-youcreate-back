package com.igrowker.miniproject.TaxObligation.Controller;
import com.igrowker.miniproject.TaxObligation.Dto.TaxDTO;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/taxes")
public class TaxController {

    private final UserService userService;

    private final TaxService taxService;

    public TaxController(UserService userService, TaxService taxService) {
        this.userService = userService;
        this.taxService = taxService;
    }

    // Endpoint para obtener todos los IVAs
    @GetMapping
    public ResponseEntity<List<TaxDTO>> getAllTaxRates() {
        return ResponseEntity.ok(taxService.getAllTaxRates());
    }

    @GetMapping("/{username}")
    public ResponseEntity<String> getTaxRateForUser(@PathVariable String username) {
        // Recuperar el usuario por nombre de usuario
        UserEntity user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Obtenga el IVA según el país del usuario
        String country = user.getCountry();
        Double taxRate = taxService.getTaxRateForCountry(country);

        if (taxRate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tax rate for country " + country + " is not available");
        }

        // Preparar el mensaje de respuesta
        String responseMessage = "User: " + user.getUserName() + ", Tax rate for " + country + " is: " + taxRate + "%";
        return ResponseEntity.ok(responseMessage);
    }
}
