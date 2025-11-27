package com.mydeseret.mydeseret.dto;

import java.util.HashSet;
import java.util.Set;

import com.mydeseret.mydeseret.model.enums.ApplicationPermission;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
// import lombok.Data;

// @Data
public class UserRequestDto {

    @NotNull(message = "Username should not be empty or null")
    private String userName;

    @NotNull(message = "Firstname should not be empty or null")
    private String first_name;

    @NotNull(message = "Last name should not be empty or null")
    private String last_name;

    @NotNull(message = "Email should not be empty or null")
    @Email
    private String email;

    @NotNull(message = "Password should not be empty or null")
    private String password; // The raw user password input

    @NotNull(message = "Business name should not be empty or null")
    private String business_name;

    private String timeZone;

    // This will receive a list of ids [1,2] etc.
    private Set<Integer> roleIds = new HashSet<>();

    private Set<ApplicationPermission> customPermissions = new HashSet<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusinessName() {
        return business_name;
    }

    public void setBusinessName(String business_name) {
        this.business_name = business_name;
    }

    public Set<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<ApplicationPermission> getCustomPermissions() {
        return customPermissions;
    }

    public void setCustomPermissions(Set<ApplicationPermission> customPermissions) {
        this.customPermissions = customPermissions;
    }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }
    
}