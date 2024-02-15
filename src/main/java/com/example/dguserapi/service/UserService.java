package com.example.dguserapi.service;

import com.example.dguserapi.dto.UserReadDtoRequest;
import com.example.dguserapi.dto.UserReadDtoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class UserService {

    @Value("${users.api.url}")
    private String USERS_URL;

    private final RestTemplate restTemplate;

    public ResponseEntity<UserReadDtoResponse> findById(UserReadDtoRequest userReadDtoRequest) {
        return restTemplate.postForEntity(USERS_URL, userReadDtoRequest, UserReadDtoResponse.class);
    }
}
