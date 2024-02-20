package com.example.dguserapi.service;

import com.example.dguserapi.database.entity.Person;
import com.example.dguserapi.database.repository.PersonRepository;
import com.example.dguserapi.dto.PersonCreateEditDto;
import com.example.dguserapi.dto.PersonReadDto;
import com.example.dguserapi.mapper.PersonCreateEditMapper;
import com.example.dguserapi.mapper.PersonReadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService implements UserDetailsService {

    private final PersonCreateEditMapper personCreateEditMapper;
    private final PersonReadMapper personReadMapper;
    private final PersonRepository personRepository;

    @Transactional
    public PersonReadDto create(PersonCreateEditDto personCreateEditDto) {
        log.info("Create person");
        return Optional.of(personCreateEditDto)
                .map(personCreateEditMapper::map)
                .map(personRepository::save)
                .map(personReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public PersonReadDto update(Long id, PersonCreateEditDto personCreateEditDto) {
        return personRepository.findById(id)
                .map(entity -> personCreateEditMapper.map(personCreateEditDto, entity))
                .map(personRepository::saveAndFlush) //сохраняем в БД
                .map(personReadMapper::map)
                .orElseThrow();
    }

    public Optional<Person> findPersonByLogin(String login) {
        return personRepository.findByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.findByLogin(username)
                .map(user -> new User(
                        user.getLogin(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
