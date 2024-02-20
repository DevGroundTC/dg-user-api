package com.example.dguserapi.mapper;

import com.example.dguserapi.database.entity.Person;
import com.example.dguserapi.dto.PersonReadDto;
import org.springframework.stereotype.Component;

@Component
public class PersonReadMapper implements Mapper<Person, PersonReadDto> {
    @Override
    public PersonReadDto map(Person object) {
        return new PersonReadDto(
                object.getId(),
                object.getUserProfileId(),
                object.getLogin(),
                object.getRole()
        );
    }
}
