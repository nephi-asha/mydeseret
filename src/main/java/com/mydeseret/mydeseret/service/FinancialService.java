package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.FinancialReportDto;
import com.mydeseret.mydeseret.repository.ExpenseRepository;
import com.mydeseret.mydeseret.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class FinancialService {

    @Autowired private SaleRepository saleRepository;
    @Autowired private ExpenseRepository expenseRepository;

    public FinancialReportDto generatePnl(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        // Revenue
        BigDecimal revenue = saleRepository.calculateTotalRevenue(start, end);

        // COGS (Direct Cost of Items Sold)
        BigDecimal cogs = saleRepository.calculateTotalCOGS(start, end);

        // Gross Profit
        BigDecimal grossProfit = revenue.subtract(cogs);

        // Operating Expenses (Rent, Payroll, etc.)
        BigDecimal expenses = expenseRepository.calculateTotalExpenses(startDate, endDate);

        // Net Profit
        BigDecimal netProfit = grossProfit.subtract(expenses);

        // Margin % (Net Profit / Revenue)
        String margin = "0%";
        if (revenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal marginValue = netProfit.divide(revenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            margin = marginValue.setScale(2, RoundingMode.HALF_UP) + "%";
        }

        FinancialReportDto report = new FinancialReportDto();
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        report.setTotalRevenue(revenue);
        report.setCostOfGoodsSold(cogs);
        report.setGrossProfit(grossProfit);
        report.setTotalExpenses(expenses);
        report.setNetProfit(netProfit);
        report.setProfitMargin(margin);

        return report;
    }
}