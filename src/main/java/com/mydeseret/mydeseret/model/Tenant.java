package com.mydeseret.mydeseret.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
// import lombok.Data;

@Entity
@Table(name = "tenants", schema = "public")
// @Data
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Long tenantId;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "tenant_name")
    @NotNull
    private String tenantName;

    @Column(name = "time_zone")
    private String timeZone = "UTC";

    @Column(name = "schema_name", unique = true)
    @NotNull
    private String schemaName;

    @Column(name = "created_at")
    @NotNull
    private LocalDate creeatedDate = LocalDate.now();

    @Column(name = "updated_at")
    @NotNull
    private LocalDate updatedDate = LocalDate.now();

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public LocalDate getCreeatedDate() {
        return creeatedDate;
    }

    public void setCreeatedDate(LocalDate creeatedDate) {
        this.creeatedDate = creeatedDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getTimeZone() { return timeZone; }
    public void setTimeZone(String timeZone) { this.timeZone = timeZone; }    
}
