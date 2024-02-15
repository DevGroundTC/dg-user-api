package com.example.dguserapi.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class UserReadDtoResponse {
    Long requestId;
    Long id;
    String nickname;
    String firstname;
    String lastname;
    String location;
    String birthPlace;
    LocalDateTime birthDate;
    String sex;
    LocalDateTime created;
    LocalDateTime modified;
    String marriedStatus;
    String educationLevel;
}