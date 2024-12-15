package com.web.pocketbuddy.controller;

import com.web.pocketbuddy.dto.ClientResponse;
import com.web.pocketbuddy.payload.ClientCredential;
import com.web.pocketbuddy.payload.RegisterClient;
import com.web.pocketbuddy.service.ClientServices;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/user")
@AllArgsConstructor
public class AuthenticationController {

    private final ClientServices clientServices;

    @PostMapping("/register")
    public ResponseEntity<ClientResponse> registerClient(@RequestBody RegisterClient client) {
        return new ResponseEntity<>(clientServices.registerClient(client), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateClient(@RequestBody ClientCredential credential) {
        return ResponseEntity.ok(clientServices.authenticateClient(credential));
    }

    @GetMapping("/email/verify")
    public ResponseEntity<String> verifyEmailAddress(@RequestParam("verificationToken") String token) {
        clientServices.verifyEmailAddress(token);
        return ResponseEntity.ok("Email address has been verified");
    }

    @GetMapping("re-verify/email")
    public ResponseEntity<String> reVerifyEmailAddress(@RequestParam String emailOrPhone) {
        return ResponseEntity.ok(clientServices.reVerifyEmailAddress(emailOrPhone));
    }

}
