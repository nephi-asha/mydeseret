package com.mydeseret.mydeseret.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.mydeseret.mydeseret.model.enums.ApplicationPermission;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
// import lombok.Data;

@Entity
@Table(name = "roles", schema = "public")
// @Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @NotNull
    @Column(name = "role_name")
    private String roleName;

    @NotNull
    @Column(name = "created_at")
    private LocalDate createdDate = LocalDate.now();

    @NotNull
    @Column(name = "updated_at")
    private LocalDate updatedDate = LocalDate.now();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name= "role_id"))
    @Column(name = "permission")
    private Set<ApplicationPermission> permissions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = true)
    private Tenant tenant;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public Set<ApplicationPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<ApplicationPermission> permissions) {
        this.permissions = permissions;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    
}
