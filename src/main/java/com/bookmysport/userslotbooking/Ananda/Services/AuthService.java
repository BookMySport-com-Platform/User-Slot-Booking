package com.bookmysport.userslotbooking.Ananda.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

    @Value("${SECRET_KEY}")
    private static String secretKey;

    public String generateToken(String email) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);

            String token = Jwts.builder()
                    .setSubject(email)
                    .signWith(Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                    .compact();

            return token;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String verifyToken(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(token);

        return claims.getBody().getSubject();
    }
}
