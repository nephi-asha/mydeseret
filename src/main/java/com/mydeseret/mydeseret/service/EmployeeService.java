package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.EmployeeRequestDto;
import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.model.enums.EmployeeStatus;
import com.mydeseret.mydeseret.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired private UserRepository userRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional
    public Employee hireEmployee(EmployeeRequestDto request) {
        User manager = getAuthenticatedUser();
        Tenant tenant = manager.getTenant();

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setUserName(request.getEmail()); // Username will be Email for employees
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        newUser.setBusinessName(tenant.getTenantName());
        newUser.setActive(true); // Employees are active immediately
        newUser.setApprovalToken(UUID.randomUUID().toString()); // Dummy token for now

        Role employeeRole = roleRepository.findByRoleName("EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("EMPLOYEE Role not found. Seed DB!"));
        newUser.setRoles(Collections.singleton(employeeRole));

        newUser = userRepository.save(newUser);

        // Creates the Employee Profile
        Employee employee = new Employee();
        employee.setUser(newUser);
        employee.setJobTitle(request.getJobTitle());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());
        employee.setHireDate(LocalDate.now());
        employee.setStatus(EmployeeStatus.ACTIVE);

        return employeeRepository.save(employee);
    }
}