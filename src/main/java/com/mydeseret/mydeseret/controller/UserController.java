package com.mydeseret.mydeseret.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mydeseret.mydeseret.dto.LoginRequestDto;
import com.mydeseret.mydeseret.dto.LoginResponseDto;
import com.mydeseret.mydeseret.dto.UserRequestDto;
import com.mydeseret.mydeseret.dto.UserResponseDto;
import com.mydeseret.mydeseret.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
   public UserService userService;

   @Autowired
   public UserController(UserService userService) {
    this.userService = userService;
   }

   @PostMapping("/register")
   public ResponseEntity<UserResponseDto> registerUser(
    @Valid @RequestBody UserRequestDto userRequestDto
   ) {
        UserResponseDto createdUser = userService.registerUser(userRequestDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        
   }

   @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = userService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }
}
