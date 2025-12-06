package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.EmployeeRequestDto;
import com.mydeseret.mydeseret.dto.EmployeeResponseDto;
import com.mydeseret.mydeseret.mapper.EmployeeMapper;
import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.model.enums.EmployeeStatus;
import com.mydeseret.mydeseret.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RequisitionRepository requisitionRepository;

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
        newUser.setApprovalToken(UUID.randomUUID().toString());

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

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employeeMapper::toResponseDto);
    }

    @Transactional
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User user = employee.getUser();

        if (request.getFirstName() != null)
            user.setFirstName(request.getFirstName());
        if (request.getLastName() != null)
            user.setLastName(request.getLastName());
        // Changing of email/username will require re-verification

        if (request.getJobTitle() != null)
            employee.setJobTitle(request.getJobTitle());
        if (request.getDepartment() != null)
            employee.setDepartment(request.getDepartment());
        if (request.getSalary() != null)
            employee.setSalary(request.getSalary());

        return new EmployeeMapper().toResponseDto(employeeRepository.save(employee));
    }

    @Transactional
    public void terminateEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Enterprise Check: Is this person an approver for pending requests?
        boolean hasPendingApprovals = requisitionRepository.existsByApprover_UserIdAndStatus(
                employee.getUser().getUserId(),
                com.mydeseret.mydeseret.model.enums.RequisitionStatus.PENDING);

        if (hasPendingApprovals) {
            throw new RuntimeException(
                    "Cannot terminate employee. They have pending requisitions to approve. Please reassign them first.");
        }

        // Set Status to TERMINATED
        employee.setStatus(EmployeeStatus.TERMINATED);

        // Revoke Login Access
        employee.getUser().setActive(false);

        employeeRepository.save(employee);
    }

}