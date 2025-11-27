package com.mydeseret.mydeseret.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CustomerRequestDto {
    @NotNull(message = "Name is required")
    private String name;
    private String email;
    private String phone;
    private String address;
    private BigDecimal creditLimit;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
}