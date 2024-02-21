package com.example.dguserapi.service;

import com.example.dguserapi.database.entity.Person;
import com.example.dguserapi.exception.JwtTokenMalformedException;
import com.example.dguserapi.exception.JwtTokenMissingException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    public String generateToken(Person person) {
        log.info("Generate token for person {}", person.getLogin());
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + tokenValidity;
        Date exp = new Date(expMillis);
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                SIGNATURE_ALGORITHM.getJcaName());
        return Jwts.builder()
                .setSubject(person.getLogin())
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(exp)
                .signWith(hmacKey)
                .compact();
    }

    public void validateToken(final String token) throws JwtTokenMalformedException, JwtTokenMissingException {
        log.info("Token validate procedure starting...");

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                SIGNATURE_ALGORITHM.getJcaName());

        try {
            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(token);
            log.info("Claims from token: {}", jwt);
        } catch (JwtException ex) {
            log.error("Token Exception!!!");
            throw new JwtTokenMalformedException("Invalid JWT signature");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw new JwtTokenMissingException("JWT claims string is empty.");
        }
    }

    public String getUsername(String token) {

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey),
                SIGNATURE_ALGORITHM.getJcaName());

        return Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
