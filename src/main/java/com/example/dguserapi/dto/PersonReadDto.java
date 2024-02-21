package com.example.dguserapi.dto;

import com.example.dguserapi.database.entity.Role;
import lombok.Value;

@Value
public class PersonReadDto {

    Long id;
    Long userProfileId;
    String login;
    Role role;
}
