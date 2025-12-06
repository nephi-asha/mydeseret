package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.SaleRequestDto;
import com.mydeseret.mydeseret.dto.SaleResponseDto;
import com.mydeseret.mydeseret.mapper.SaleMapper;
import com.mydeseret.mydeseret.model.Sale;
import com.mydeseret.mydeseret.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@Tag(name = "Sales (POS)", description = "Point of Sale operations, Receipt generation, and Sales History")
@SecurityRequirement(name = "bearerAuth")
public class SaleController {

    @Autowired private SaleService saleService;
    @Autowired private SaleMapper saleMapper;

    @Operation(
        summary = "Process a New Sale", 
        description = "Deducts inventory, calculates totals, and records revenue. If 'CREDIT' is used, checks Customer Credit Limit."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sale completed successfully", 
            content = @Content(schema = @Schema(implementation = SaleResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input, Insufficient Stock, or Credit Limit Exceeded", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied (Requires SALE_CREATE)", content = @Content)
    })
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SALE_CREATE')")
    public ResponseEntity<SaleResponseDto> createSale(@Valid @RequestBody SaleRequestDto request) {
        Sale sale = saleService.createSale(request);
        return ResponseEntity.ok(saleMapper.toResponseDto(sale));
    }

    @Operation(summary = "Get Sales History (with Filtering)")
    @GetMapping
    @PreAuthorize("hasAuthority('SALE_READ')")
    public ResponseEntity<Page<SaleResponseDto>> getAllSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("saleDate").descending());
        
        // Will need to Converts LocalDate to LocalDateTime for query
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(23, 59, 59) : null;

        Page<SaleResponseDto> sales = saleService.getAllSales(start, end, minAmount, pageable)
                                                 .map(saleMapper::toResponseDto);
        
        return ResponseEntity.ok(sales);
    }
}