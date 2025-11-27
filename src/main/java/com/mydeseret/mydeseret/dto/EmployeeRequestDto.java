package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
// import lombok.Data;
import java.math.BigDecimal;

// @Data
public class EmployeeRequestDto {
    @NotNull private String first_name;
    @NotNull private String last_name;
    @NotNull @Email private String email;
    @NotNull private String password; // Temporary password assigned by the Owner or Admin
    
    @NotNull private String jobTitle;
    @NotNull private Department department;
    @Positive private BigDecimal salary;
    
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
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public BigDecimal getSalary() {
        return salary;
    }
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    
}