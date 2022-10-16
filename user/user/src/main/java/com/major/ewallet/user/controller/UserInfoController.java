package com.major.ewallet.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.ewallet.user.entity.UserInfo;
import com.major.ewallet.user.request.CreateUserRequestDto;
import com.major.ewallet.user.service.UserInfoService;

import lombok.SneakyThrows;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @PostMapping(value = "/createNewUser",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto){
        /*
        * Create a new user > persist in DB > send message to User Created Kafka Queue
        * */
        UserInfo newUser = userInfoService.createANewUser(createUserRequestDto);
        userInfoService.sendMessage(newUser);
        return new ResponseEntity<>(objectMapper.writeValueAsString(newUser), HttpStatus.CREATED);
    }

    @SneakyThrows
    @GetMapping(value = "/user/{id}",produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getUser(@PathVariable Long id){
        UserInfo userInfo = userInfoService.fetchUserById(id);

        return new ResponseEntity<String>(objectMapper.writeValueAsString(userInfo),HttpStatus.OK);
    }
}
