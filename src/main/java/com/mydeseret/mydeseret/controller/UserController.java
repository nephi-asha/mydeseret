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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(path = "/api/v1/users")
@io.swagger.v3.oas.annotations.tags.Tag(name = "User Management", description = "Endpoints for User Registration, Login, and Password Management")
public class UserController {
    public UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Register a new Business/User", description = "Creates a new User account. If it is the first user for a business, it creates the Tenant.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User successfully created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.registerUser(userRequestDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

    }

    @Autowired
    private com.mydeseret.mydeseret.service.ActivityLogService activityLogService;

    @io.swagger.v3.oas.annotations.Operation(summary = "Login", description = "Authenticates a user and returns a JWT Bearer Token.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest,
            jakarta.servlet.http.HttpServletRequest request) {
        LoginResponseDto response = userService.loginUser(loginRequest);

        // Log the activity if login is successful or MFA required
        userService.findUserByEmail(loginRequest.getEmail()).ifPresent(user -> {
            activityLogService.logActivity(
                    user.getUserId(),
                    "LOGIN_ATTEMPT",
                    "Status: " + response.getStatus(),
                    request.getRemoteAddr());
        });

        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Verify 2FA for Login", description = "Completes the login process by verifying the 2FA code.")
    @PostMapping("/login/verify-2fa")
    public ResponseEntity<LoginResponseDto> verify2FALogin(@RequestBody Map<String, String> request,
            jakarta.servlet.http.HttpServletRequest httpRequest) {
        String email = request.get("email");
        String code = request.get("code");

        LoginResponseDto response = userService.verifyTwoFactorLogin(email, code);

        userService.findUserByEmail(email).ifPresent(user -> {
            activityLogService.logActivity(
                    user.getUserId(),
                    "LOGIN_SUCCESS_2FA",
                    "2FA Verified",
                    httpRequest.getRemoteAddr());
        });

        return ResponseEntity.ok(response);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Forgot Password", description = "Initiates the password reset process by sending an email with a reset token.")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        userService.forgotPassword(email);
        return ResponseEntity.ok("If an account exists with that email, a reset link has been sent.");
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Reset Password", description = "Completes the password reset process using the token received via email.")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully. You can now login.");
    }
}
