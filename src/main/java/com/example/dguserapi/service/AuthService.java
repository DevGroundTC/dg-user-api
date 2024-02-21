package com.example.dguserapi.service;

import com.example.dguserapi.database.repository.PersonRepository;
import com.example.dguserapi.dto.PersonCreateEditDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseEntity<String> checkUser(PersonCreateEditDto loginPerson) {
        var authResult = new AuthResult();
        var foundPerson = personRepository.findByLogin(loginPerson.getLogin());
        if (foundPerson.isPresent()) {
            var person = foundPerson.get();
            if (passwordEncoder.matches(loginPerson.getRawPassword(), person.getPassword())) {
                String token = jwtService.generateToken(person);
                log.info("Person {} was successfully authorized. Token: {}", person.getLogin(), token);
                authResult.setHttpStatus(HttpStatus.OK);
                authResult.setResponseBody("Person ID: " + person.getId());
                authResult.setToken(token);
            } else {
                log.info("Unauthorized access for person with login {}", person.getLogin());
            }
        }
        return ResponseEntity
                .status(authResult.getHttpStatus())
                .header("Authorization", authResult.getToken())
                .body(authResult.getResponseBody());
    }
}
