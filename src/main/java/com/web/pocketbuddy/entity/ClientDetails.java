package com.web.pocketbuddy.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clientdetails")
public class ClientDetails {

    @Id
    private String clientId;

    @NotNull
    private String clientFirstName;
    private String clientLastName;

    @Email
    private String emailAddress;

    @NotNull
    private String password;

    @NotNull
    private String mobileNumber;

    @CreatedDate
    private Date createDate;

    @LastModifiedDate
    private Date lastUpdateDate;

    private String verificationToken;
    private boolean emailVerified;

    private boolean numberVerified;

    private int otp;
    private int otpCount;



}
