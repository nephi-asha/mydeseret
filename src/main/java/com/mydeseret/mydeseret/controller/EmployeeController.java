package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.EmployeeRequestDto;
import com.mydeseret.mydeseret.dto.EmployeeResponseDto;
import com.mydeseret.mydeseret.model.Employee;
import com.mydeseret.mydeseret.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_READ')")
    public ResponseEntity<Page<EmployeeResponseDto>> getEmployees(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "employeeId") String sortBy
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(employeeService.getEmployees(pageable));
    }

    @PostMapping("/hire")
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
    public ResponseEntity<Employee> hireEmployee(@Valid @RequestBody EmployeeRequestDto request) {
        return ResponseEntity.ok(employeeService.hireEmployee(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDto request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_DELETE')")
    public ResponseEntity<Void> terminateEmployee(@PathVariable Long id) {
        employeeService.terminateEmployee(id);
        return ResponseEntity.ok().build();
    }
}