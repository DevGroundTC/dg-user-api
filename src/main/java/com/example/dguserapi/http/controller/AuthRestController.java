package com.example.dguserapi.http.controller;

import com.example.dguserapi.dto.PersonCreateEditDto;
import com.example.dguserapi.dto.PersonReadDto;
import com.example.dguserapi.service.AuthService;
import com.example.dguserapi.service.PersonService;
import com.example.dguserapi.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final AuthService authService;

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
        return authService.checkUser(loginPerson);
    }
}
