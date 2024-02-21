package com.example.dguserapi.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AuthResult {

    private HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    private String responseBody = "Unauthorized";
    private String token = "";
}
