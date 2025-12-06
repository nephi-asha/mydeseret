package com.mydeseret.mydeseret.dto;

public class LoginResponseDto {
    private String token;
    private String email;
    private String role;
    private String status; // SUCCESS, MFA_REQUIRED
    private String message;

    public LoginResponseDto(String token, String email, String role, String status, String message) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.status = status;
        this.message = message;
    }

    public LoginResponseDto(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}