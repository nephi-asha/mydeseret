package com.mydeseret.mydeseret.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequestDto {
    @NotNull(message = "Description is required")
    private String description; // Something like "Annual Rent 2025"

    @NotNull
    @Positive
    private BigDecimal amount; // Total Amount (e.g. 12000)

    @NotNull
    private LocalDate expenseDate;

    private boolean amortize = false; // Checkbox: "Spread this expense?"
    
    @Positive
    private int monthsToSpread = 1; // Default 1 which means No spread

    // Getters & Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public boolean isAmortize() { return amortize; }
    public void setAmortize(boolean amortize) { this.amortize = amortize; }
    public int getMonthsToSpread() { return monthsToSpread; }
    public void setMonthsToSpread(int monthsToSpread) { this.monthsToSpread = monthsToSpread; }
}