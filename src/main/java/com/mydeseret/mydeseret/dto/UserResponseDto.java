package com.mydeseret.mydeseret.dto;

// import lombok.Data;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

// @Data
public class UserResponseDto {
    private UUID user_id;
    private String userName;
    private String email;
    private String first_name;
    private String last_name;
    private String business_name;
    private boolean is_active;
    
    private Long tenant_id;
    
    private Set<String> roles;
    
    private LocalDate createdDate;

    public UUID getUserId() {
        return user_id;
    }

    public void setUserId(UUID user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getBusinessName() {
        return business_name;
    }

    public void setBusinessName(String business_name) {
        this.business_name = business_name;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setActive(boolean is_active) {
        this.is_active = is_active;
    }

    public Long getTenantId() {
        return tenant_id;
    }

    public void setTenantId(Long tenant_id) {
        this.tenant_id = tenant_id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }    
}