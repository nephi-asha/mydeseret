package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.SaleRequestDto;
import com.mydeseret.mydeseret.model.Sale;
import com.mydeseret.mydeseret.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SALE_CREATE')")
    public ResponseEntity<Sale> createSale(@RequestBody SaleRequestDto request) {
        return ResponseEntity.ok(saleService.createSale(request));
    }

    // GET /api/v1/sales?page=0&size=20
    @GetMapping
    @PreAuthorize("hasAuthority('SALE_READ')")
    public ResponseEntity<Page<Sale>> getAllSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        // Sort by date descending (newest sales first)
        PageRequest pageable = PageRequest.of(page, size, Sort.by("saleDate").descending());
        return ResponseEntity.ok(saleService.getAllSales(pageable));
    }
}