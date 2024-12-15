package com.web.pocketbuddy.service.impl;

import org.springframework.scheduling.annotation.Async;

public class EmailService {

    @Async
    public static void sendEmailVerificationToken(String email) {
        System.out.println("Sending email verification token to: " + email);
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];

        String maskedUsername = username.charAt(0) + "*".repeat(username.length() - 1);

        return maskedUsername + "@" + domain;
    }

}
