package com.web.pocketbuddy.service;

import com.web.pocketbuddy.dto.ClientResponse;
import com.web.pocketbuddy.payload.ClientCredential;
import com.web.pocketbuddy.payload.RegisterClient;

import java.util.List;

public interface ClientServices {

    public ClientResponse registerClient(RegisterClient registerClient);

    public String authenticateClient(ClientCredential credential);

    public ClientResponse getClientDetails(String emailOrPhone);

    public List<ClientResponse> getAllClientDetails(String key);

    public void verifyEmailAddress(String token);
    public String reVerifyEmailAddress(String emailOrPhone);
}
