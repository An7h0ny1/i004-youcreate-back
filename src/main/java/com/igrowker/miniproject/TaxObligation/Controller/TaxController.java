package com.igrowker.miniproject.TaxObligation.Controller;
import com.igrowker.miniproject.TaxObligation.Service.TaxService;
import com.igrowker.miniproject.User.Model.UserEntity;
import com.igrowker.miniproject.User.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/taxes")
public class TaxController {

    private final UserService userService;

    private final TaxService taxService;

    public TaxController(UserService userService, TaxService taxService) {
        this.userService = userService;
        this.taxService = taxService;
    }

    /*
    @GetMapping("/{username}")
    public ResponseEntity<?> getTaxRateForUser(@PathVariable String username) {
        // Fetch user by username
        Optional<UserEntity> userOptional = userService.findByUsername(username);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
        }

        UserEntity user = userOptional.get();

        // Get country and tax rate
        String country = user.getCountry();
        Double taxRate = user.getTaxRate(); // Assuming this is already populated

        // Construct response message
        String message = "Tax rate for " + country + " is: " + taxRate + "%";

        // Return response
        Map<String, String> response = new HashMap<>();
        response.put("username", user.getUserName());
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{country}")
    public ResponseEntity<String> getTaxRate(@PathVariable String country) {
        Double taxRate = taxService.getTaxRate(country);
        if (taxRate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tax rate not found for country: " + country);
        }
        return ResponseEntity.ok("Tax rate for " + country + " is: " + taxRate + "%");
    }

    @GetMapping("/{getAllTaxRates}")
    public ResponseEntity<Map<String, Double>> getAllTaxRates() {
        Map<String, Double> taxRates = taxService.getAllTaxRates();
        return ResponseEntity.ok(taxRates);
    }
    */

    @GetMapping("/{username}")
    public ResponseEntity<String> getTaxRateForUser(@PathVariable String username) {
        // Retrieve the user by username
        UserEntity user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Get the tax rate based on the user's country
        String country = user.getCountry();
        Double taxRate = taxService.getTaxRateForCountry(country);

        if (taxRate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tax rate for country " + country + " is not available");
        }

        // Prepare the response message
        String responseMessage = "User: " + user.getUserName() + ", Tax rate for " + country + " is: " + taxRate + "%";
        return ResponseEntity.ok(responseMessage);
    }
}
