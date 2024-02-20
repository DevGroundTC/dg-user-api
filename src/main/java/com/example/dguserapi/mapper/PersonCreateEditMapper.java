package com.example.dguserapi.mapper;

import com.example.dguserapi.database.entity.Person;
import com.example.dguserapi.dto.PersonCreateEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PersonCreateEditMapper implements Mapper<PersonCreateEditDto, Person> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Person map(PersonCreateEditDto personCreateEditDto) {
        Person person = new Person();
        copy(personCreateEditDto, person);
        return person;
    }

    @Override
    public Person map(PersonCreateEditDto fromObject, Person toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(PersonCreateEditDto object, Person person) {
        person.setLogin(object.getLogin());

        Optional.ofNullable(object.getRawPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(person::setPassword);

        person.setRole(object.getRole());
    }
}
