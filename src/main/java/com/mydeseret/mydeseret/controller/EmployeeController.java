package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.EmployeeRequestDto;
import com.mydeseret.mydeseret.model.Employee;
import com.mydeseret.mydeseret.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/hire")
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
    public ResponseEntity<Employee> hireEmployee(@Valid @RequestBody EmployeeRequestDto request) {
        return ResponseEntity.ok(employeeService.hireEmployee(request));
    }
}