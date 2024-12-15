package com.web.pocketbuddy.controller;

import com.web.pocketbuddy.dto.ClientResponse;
import com.web.pocketbuddy.service.ClientServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class ClientController {

    private final ClientServices clientServices;

    public ClientController(ClientServices clientServices) {
        this.clientServices = clientServices;
    }

    @GetMapping
    public ResponseEntity<ClientResponse> getClientData(@RequestParam String emailOrPhone) {
        return ResponseEntity.ok(clientServices.getClientDetails(emailOrPhone));
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientResponse>> getAllClientData(@RequestParam("apiKey") String key) {
        return ResponseEntity.ok(clientServices.getAllClientDetails(key));
    }

}
