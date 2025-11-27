package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.Department;
import com.mydeseret.mydeseret.model.enums.EmployeeStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String jobTitle;
    private Department department;
    private EmployeeStatus status;
    private BigDecimal salary;
    private LocalDate hireDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public EmployeeStatus getStatus() { return status; }
    public void setStatus(EmployeeStatus status) { this.status = status; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
}