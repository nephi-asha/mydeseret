package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.config.TenantContext;
import com.mydeseret.mydeseret.dto.FinancialReportDto;
import com.mydeseret.mydeseret.service.FinancialService;
import com.mydeseret.mydeseret.service.PdfService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
public class FinancialController {

    @Autowired private FinancialService financialService;
    @Autowired private PdfService pdfService;

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

    @Operation(summary = "Download P&L as PDF", description = "Returns a binary PDF file for printing.")
    @GetMapping("/pnl/pdf")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_READ')")
    public ResponseEntity<byte[]> downloadPnlPdf(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().withDayOfMonth(1);
        if (endDate == null) endDate = LocalDate.now();

        FinancialReportDto report = financialService.generatePnl(startDate, endDate);
        
        String businessName = TenantContext.getCurrentTenant();
        if (businessName != null) {
            // Converts "deseret_bakery" to "Deseret Bakery"
            businessName = businessName.replace("_", " ").toUpperCase();
        } else {
            businessName = "MY BUSINESS";
        }

        // Generate PDF
        byte[] pdfBytes = pdfService.generatePnlPdf(report, businessName);

        // Return File
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pnl_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }    
}