package com.example.dguserapi.http.controller;

import com.example.dguserapi.dto.UserReadDtoRequest;
import com.example.dguserapi.dto.UserReadDtoResponse;
import com.example.dguserapi.service.UserService;
import com.example.dguserapi.util.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello world";
    }

    @PostMapping
    public ResponseEntity<UserReadDtoResponse> findUserById(@RequestBody UserReadDtoRequest userReadDtoRequest) {
        log.info("Get user from users by rest. Incoming JSON: " + System.lineSeparator() + JacksonUtil.fromObjectToJson(userReadDtoRequest));
        var result = userService.findById(userReadDtoRequest);
        log.info("Returning Result: " + System.lineSeparator() + JacksonUtil.fromObjectToJson(result.getBody()));
        return result;
    }
}
