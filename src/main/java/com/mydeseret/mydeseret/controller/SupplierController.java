package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.SupplierRequestDto;
import com.mydeseret.mydeseret.dto.SupplierResponseDto;
import com.mydeseret.mydeseret.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    @PreAuthorize("hasAuthority('SUPPLIER_CREATE')")
    public ResponseEntity<SupplierResponseDto> createSupplier(@Valid @RequestBody SupplierRequestDto request) {
        return ResponseEntity.ok(supplierService.createSupplier(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPPLIER_READ')")
    public ResponseEntity<Page<SupplierResponseDto>> getSuppliers(Pageable pageable) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(pageable));
    }
}