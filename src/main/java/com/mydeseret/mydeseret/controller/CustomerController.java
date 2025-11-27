package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.CustomerRequestDto;
import com.mydeseret.mydeseret.dto.CustomerResponseDto;
import com.mydeseret.mydeseret.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public ResponseEntity<Page<CustomerResponseDto>> getCustomers(Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }
}