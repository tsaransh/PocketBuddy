package com.web.pocketbuddy.dto;

import java.util.Date;

public record ClientResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String mobileNumber,
        Date createDate
) {
}
