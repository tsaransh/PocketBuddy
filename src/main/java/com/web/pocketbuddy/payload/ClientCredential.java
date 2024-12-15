package com.web.pocketbuddy.payload;

public record ClientCredential(
        String emailOrPhone,
        String password
) {
}
