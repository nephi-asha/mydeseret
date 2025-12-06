package com.mydeseret.mydeseret.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FinancialReportDto {
    private LocalDate startDate;
    private LocalDate endDate;
    
    private BigDecimal totalRevenue;   // Sales
    private BigDecimal costOfGoodsSold; // COGS (Direct Costs)
    private BigDecimal grossProfit;    // Revenue - COGS
    
    private BigDecimal totalExpenses;  // Operating Expenses (Rent, Salaries)
    private BigDecimal netProfit;      // Gross Profit - Expenses
    
    private String profitMargin;

    private BigDecimal totalCOGS;

    public void setTotalCOGS(BigDecimal totalCOGS) {
        this.totalCOGS = totalCOGS;
    }

    public BigDecimal getTotalCOGS() {
        return totalCOGS;
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public BigDecimal getCostOfGoodsSold() { return costOfGoodsSold; }
    public void setCostOfGoodsSold(BigDecimal costOfGoodsSold) { this.costOfGoodsSold = costOfGoodsSold; }
    public BigDecimal getGrossProfit() { return grossProfit; }
    public void setGrossProfit(BigDecimal grossProfit) { this.grossProfit = grossProfit; }
    
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }
    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }
    public String getProfitMargin() { return profitMargin; }
    public void setProfitMargin(String profitMargin) { this.profitMargin = profitMargin; }
}