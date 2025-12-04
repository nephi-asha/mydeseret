package com.mydeseret.mydeseret.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;

import com.mydeseret.mydeseret.model.enums.ApplicationPermission;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
// import lombok.Data;

// @Data
@Entity
@Table(name = "users", schema = "public")
@Audited
public class User {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "username", length = 100, unique = true, updatable = false)
    @NotNull
    private String username;

    @Column(name = "first_name", length = 100)
    @NotNull
    private String firstName;

    @Column(name = "last_name", length = 100)
    @NotNull
    private String lastName;

    @Column(name = "email", length = 255, unique = true)
    @NotNull
    private String email;

    @Column(name = "password_hash", length = 255)
    @NotNull
    private String passwordHash;

    @Column(name = "business_name", length = 150, unique = true)
    @NotNull
    private String businessName;

    @Column(name = "is_active")
    @NotNull
    private boolean isActive = false;

    @Column(name =  "approval_token")
    private String approvalToken;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;    

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
        )
    private Set<Role> roles = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "user_custom_permissions",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "permission")
    @NotNull
    private Set<ApplicationPermission> customPermissions = new HashSet<>();

    @Column(name = "created_at")
    @NotNull
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "updated_at")
    @NotNull
    private LocalDate updatedDate = LocalDate.now();

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public boolean is_active() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getApprovalToken() {
        return approvalToken;
    }

    public void setApprovalToken(String approvalToken) {
        this.approvalToken = approvalToken;
    }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }
    public LocalDateTime getResetTokenExpiry() { return resetTokenExpiry; }
    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<ApplicationPermission> getCustomPermissions() {
        return customPermissions;
    }

    public void setCustomPermissions(Set<ApplicationPermission> customPermissions) {
        this.customPermissions = customPermissions;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

}
