package com.web.pocketbuddy.service.impl;

import com.web.pocketbuddy.dao.ClientRepository;
import com.web.pocketbuddy.dto.ClientResponse;
import com.web.pocketbuddy.exceptions.JwtTokenException;
import com.web.pocketbuddy.payload.ClientCredential;
import com.web.pocketbuddy.payload.RegisterClient;
import com.web.pocketbuddy.entity.ClientDetails;
import com.web.pocketbuddy.exceptions.ClientException;
import com.web.pocketbuddy.service.ClientServices;
import com.web.pocketbuddy.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService implements ClientServices {

    private final ClientRepository clientDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ClientResponse registerClient(RegisterClient registerClient) {

        if(registerClient == null || ObjectUtils.isEmpty(registerClient))
            throw new ClientException("failed to create an account, please the the provided information.", HttpStatus.BAD_REQUEST);

        if(clientDao.findByEmailAddress(registerClient.email()).isPresent())
            throw new ClientException("Email address is already register.", HttpStatus.BAD_REQUEST);


        if(clientDao.findByMobileNumber(registerClient.mobileNumber()).isPresent())
            throw new ClientException("Mobile number is already register.", HttpStatus.BAD_REQUEST);


        ClientDetails clientDetails = toClientDetails(registerClient);
        clientDetails.setClientId(clientId());
        clientDetails.setPassword(passwordEncoder.encode(registerClient.password()));
        clientDetails.setCreateDate(new Date());

        clientDetails.setVerificationToken(generateEmailVerificationToken());
        clientDetails.setEmailVerified(false);
        clientDetails.setNumberVerified(false);

        ClientDetails savedClient = clientDao.save(clientDetails);

        EmailService.sendEmailVerificationToken(savedClient.getEmailAddress());

        return toClientResponse(savedClient);
    }

    @Override
    public String authenticateClient(ClientCredential credential) {

        if(ObjectUtils.isEmpty(credential)
                && credential.emailOrPhone().isEmpty()
                && credential.password().isEmpty())
            throw new ClientException("Please enter you login credential", HttpStatus.BAD_REQUEST);

        ClientDetails clientDetails = clientDao.findByMobileNumberOrEmailAddress(credential.emailOrPhone(), credential.emailOrPhone())
                .orElseThrow(() -> new ClientException("No User found with: "+credential.emailOrPhone(), HttpStatus.BAD_REQUEST));

        if(passwordEncoder.matches(credential.password(), clientDetails.getPassword())) {
            return "Authentication token";
        }

        return "Invalid Client Credentials";
    }

    @Override
    public ClientResponse getClientDetails(String emailOrPhone) {
        if(StringUtils.isEmpty(emailOrPhone)) {
            throw new ClientException("Username is empty", HttpStatus.BAD_REQUEST);
        }

        ClientDetails savedClient = clientDao.findByMobileNumberOrEmailAddress(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new ClientException("No user found with id: "+emailOrPhone, HttpStatus.BAD_REQUEST));


        return toClientResponse(savedClient);
    }

    @Override
    public List<ClientResponse> getAllClientDetails(String key) {

        if(StringUtils.isEmpty(key)) {
            throw  new JwtTokenException("Please enter the key", HttpStatus.BAD_REQUEST);
        }

        if(key.compareTo(Constants.API_KEY) != 0) {
            throw  new JwtTokenException("Invalid api key", HttpStatus.BAD_REQUEST);
        }

        List<ClientDetails> savedClients = clientDao.findAll();

        return savedClients.stream().map(this::toClientResponse).toList();
    }

    @Override
    public void verifyEmailAddress(String token) {
        ClientDetails savedClient = clientDao.findByVerificationToken(token)
                .orElseThrow(() -> new ClientException("Invalid token", HttpStatus.BAD_REQUEST));

        savedClient.setEmailVerified(true);
        savedClient.setVerificationToken("");

        clientDao.save(savedClient);
    }

    @Override
    public String reVerifyEmailAddress(String emailOrPhone) {
        if(StringUtils.isEmpty(emailOrPhone)) {
            throw new ClientException("Username is empty", HttpStatus.BAD_REQUEST);
        }

        ClientDetails savedClient = clientDao.findByMobileNumberOrEmailAddress(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new ClientException("No user found with id: "+emailOrPhone, HttpStatus.BAD_REQUEST));

        if(savedClient.isEmailVerified()) {
            throw new ClientException("Your email is already verified", HttpStatus.BAD_REQUEST);
        }

        String token = generateEmailVerificationToken();

        savedClient.setVerificationToken(token);

        EmailService.sendEmailVerificationToken(token);

        clientDao.save(savedClient);

        return "An verification email sent to register email address: "
                .concat(EmailService.maskEmail(savedClient.getEmailAddress()));

    }

    private String generateEmailVerificationToken() {
        return UUID.randomUUID().toString();
    }


    private ClientDetails toClientDetails(RegisterClient client) {
        return ClientDetails.builder()
                .clientFirstName(client.firstName())
                .clientLastName(client.lastName())
                .emailAddress(client.email())
                .mobileNumber(client.mobileNumber())
                .build();
    }

    private ClientResponse toClientResponse(ClientDetails client) {
        return new ClientResponse(
                client.getClientId(),
                client.getClientFirstName(),
                client.getClientLastName(),
                client.getEmailAddress(),
                client.getMobileNumber(),
                client.getCreateDate()
        );
    }

    private String clientId() {
        return UUID.randomUUID().toString();
    }



}
