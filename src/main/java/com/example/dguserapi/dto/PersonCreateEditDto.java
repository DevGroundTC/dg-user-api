package com.example.dguserapi.dto;

import com.example.dguserapi.database.entity.Role;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
public class PersonCreateEditDto {

    @Email
    String login;

    @NotBlank
    String rawPassword;

    Role role;

}
