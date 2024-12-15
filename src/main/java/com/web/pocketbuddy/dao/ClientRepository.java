package com.web.pocketbuddy.dao;

import com.web.pocketbuddy.entity.ClientDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<ClientDetails, String> {

    Optional<ClientDetails> findByMobileNumber(String mobileNumber);
    Optional<ClientDetails> findByEmailAddress(String emailAddress);

    Optional<ClientDetails> findByMobileNumberOrEmailAddress(String mobileNumber, String emailAddress);

    Optional<ClientDetails> findByVerificationToken(String token);
}
