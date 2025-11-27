package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.FinancialReportDto;
import com.mydeseret.mydeseret.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
public class FinancialController {

    @Autowired
    private FinancialService financialService;

    // GET /api/v1/reports/pnl?startDate=2025-11-01&endDate=2025-11-30
    @GetMapping("/pnl")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_READ')")
    public ResponseEntity<FinancialReportDto> getProfitAndLoss(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        // Default to Current Month if no dates provided
        if (startDate == null) startDate = LocalDate.now().withDayOfMonth(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(financialService.generatePnl(startDate, endDate));
    }
}