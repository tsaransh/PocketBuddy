package com.web.pocketbuddy.utils;

import com.web.pocketbuddy.exceptions.JwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final long JWT_TOKEN_VALIDITY = 6 * 60 * 60 * 1000L;
    private static final String SECRET = "jN8n2B1TvTn3bR/lWo5x4M1ZKjLe0pRf3XLKgA1ErcJ1sXb7QIlRn2HyFQdJr0Og8BW+XY3Or5kFgbswMbwRJw==";

    public static String getDeviceIdFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public static Date getExpirationDateFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getExpiration();
    }

    public static Date getIssueDateFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getIssuedAt();
    }

    public static <T> T getAllClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token) {
        if(token.contains("Bearer ")) {
            token = token.replace("Bearer", "");
        }
        return Jwts.parser().setSigningKey(SECRET).build().parseClaimsJws(token).getPayload();
    }

    private static Boolean isTokenExpired(String token) {
        final Date issueDate = getIssueDateFromToken(token);
        return new Date(System.currentTimeMillis()).after(
                new Date(Long.sum(issueDate.getTime(), JWT_TOKEN_VALIDITY))
        );
    }

    private static Boolean isTokenExpiry(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(expirationDate);
    }

    public static String generateToken(Authentication authentication) {

//        Map<String, Object> claims = new LinkedHashMap<>();
//        claims.put("username", authentication.getName());
//        claims.put("roles", authentication.getAuthorities());

        return Jwts.builder()
//                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static Boolean validateToken(String token) {
        if(StringUtils.isEmpty(token)) {
            throw new JwtTokenException("No authentication token found", HttpStatus.NOT_FOUND);
        }

        token = token.substring(7);
        final String deviceId = getDeviceIdFromToken(token);

        return (deviceId != null && !isTokenExpired(token));

    }

}
