package com._pearls.contactms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${jwt.secret}") String base64SecretKey) {
        System.out.println("SECRET LENGTH (raw string): " + base64SecretKey.length());
        System.out.println("SECRET VALUE: [" + base64SecretKey + "]"); // brackets reveal whitespace
        byte[] keyBytes = Decoders.BASE64.decode(base64SecretKey);
        System.out.println("DECODED BYTES: " + keyBytes.length);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String identifier) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(identifier)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //10hours
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String extractIdentifier(String token){
        // extract the identifier from JWTToken
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String identifier = extractIdentifier(token);
        return (identifier.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

}
