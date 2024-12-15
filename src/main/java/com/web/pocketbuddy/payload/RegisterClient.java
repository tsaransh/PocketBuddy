package com.web.pocketbuddy.payload;

public record RegisterClient(
        String firstName,
        String lastName,
        String email,
        String mobileNumber,
        String password
) {
}
