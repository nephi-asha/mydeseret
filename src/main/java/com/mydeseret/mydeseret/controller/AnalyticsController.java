package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.TopItemDto;
import com.mydeseret.mydeseret.repository.SaleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Business Analytics", description = "Insights into business performance")
public class AnalyticsController {

    @Autowired
    private com.mydeseret.mydeseret.service.messaging.ReportProducer reportProducer;

    @Autowired
    private com.mydeseret.mydeseret.repository.ReportRequestRepository reportRequestRepository;

    @Autowired
    private com.mydeseret.mydeseret.repository.UserRepository userRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private com.mydeseret.mydeseret.service.FinancialService financialService;

    @Operation(summary = "Generate PnL Report (Async)", description = "Triggers generation of PnL PDF. Returns a Report ID to track status.")
    @PostMapping("/reports/generate")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_READ')")
    public ResponseEntity<Map<String, Object>> generateReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        if (startDate == null)
            startDate = LocalDateTime.now().minusDays(30);
        if (endDate == null)
            endDate = LocalDateTime.now();

        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getName();
        com.mydeseret.mydeseret.model.User user = userRepository.findByEmail(email).orElseThrow();

        com.mydeseret.mydeseret.model.ReportRequest request = new com.mydeseret.mydeseret.model.ReportRequest();
        request.setTenantId(com.mydeseret.mydeseret.config.TenantContext.getCurrentTenant());
        request.setUserId(user.getUserId());
        request.setReportType("PNL");
        request.setStatus("PENDING");
        request = reportRequestRepository.save(request);

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate.toString());
        params.put("endDate", endDate.toString());

        reportProducer.sendReportRequest(request.getId(), request.getTenantId(), "PNL", params);

        return ResponseEntity.accepted().body(Map.of(
                "message", "Report generation started",
                "reportId", request.getId(),
                "statusUrl", "/api/v1/analytics/reports/" + request.getId()));
    }

    @Operation(summary = "Get Report Status", description = "Checks status of a report. If COMPLETED, returns download URL.")
    @GetMapping("/reports/{id}")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_READ')")
    public ResponseEntity<com.mydeseret.mydeseret.model.ReportRequest> getReportStatus(
            @PathVariable java.util.UUID id) {
        return reportRequestRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get Sales Performance", description = "Returns Revenue, COGS, Net Profit, and Top Selling Items.")
    @GetMapping("/sales")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_READ')")
    public ResponseEntity<Map<String, Object>> getSalesAnalytics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        if (startDate == null)
            startDate = LocalDateTime.now().minusDays(30);
        if (endDate == null)
            endDate = LocalDateTime.now();

        com.mydeseret.mydeseret.dto.FinancialReportDto report = financialService.generatePnl(startDate.toLocalDate(),
                endDate.toLocalDate());
        List<TopItemDto> topItems = saleRepository.findTopSellingItems(startDate, endDate, PageRequest.of(0, 5));

        Map<String, Object> response = new HashMap<>();
        response.put("periodStart", startDate);
        response.put("periodEnd", endDate);
        response.put("totalRevenue", report.getTotalRevenue());
        response.put("totalCOGS", report.getCostOfGoodsSold());
        response.put("netProfit", report.getNetProfit());
        response.put("topSellingItems", topItems);

        return ResponseEntity.ok(response);
    }
}
