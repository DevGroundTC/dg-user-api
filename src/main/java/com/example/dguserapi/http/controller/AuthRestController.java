package com.example.dguserapi.http.controller;

import com.example.dguserapi.dto.PersonCreateEditDto;
import com.example.dguserapi.dto.PersonReadDto;
import com.example.dguserapi.service.JwtService;
import com.example.dguserapi.service.PersonService;
import com.example.dguserapi.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthRestController {

    private final PersonService personService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public PersonReadDto register(@RequestBody PersonCreateEditDto personCreateEditDto) {
        log.info("Register person. Incoming JSON: " + System.lineSeparator() + JacksonUtil.fromObjectToJson(personCreateEditDto));
        var createdPerson = personService.create(personCreateEditDto);
        log.info("Returning result: " + System.lineSeparator() + JacksonUtil.fromObjectToJson(createdPerson));
        return createdPerson;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody PersonCreateEditDto loginPerson) {
        log.info("Login person. Incoming JSON: " + System.lineSeparator() + JacksonUtil.fromObjectToJson(loginPerson));
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        String responseBody = "Unauthorized";
        String token = "";
        var foundPerson = personService.findPersonByLogin(loginPerson.getLogin());
        if (foundPerson.isPresent()) {
            var person = foundPerson.get();
            if (passwordEncoder.matches(loginPerson.getRawPassword(), person.getPassword())) {
                token = jwtService.generateToken(person);
                log.info("Person {} was successfully authorized. Token: {}", person.getLogin(), token);
                httpStatus = HttpStatus.OK;
                responseBody = "Person ID: " + person.getId();
            } else {
                log.info("Unauthorized access for person with login {}", person.getLogin());
            }
        }
        return ResponseEntity
                .status(httpStatus)
                .header("Authorization", token)
                .body(responseBody);
    }
}
