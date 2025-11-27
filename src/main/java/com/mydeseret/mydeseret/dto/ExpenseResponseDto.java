package com.mydeseret.mydeseret.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseResponseDto {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private String relatedPoNumber;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getRelatedPoNumber() { return relatedPoNumber; }
    public void setRelatedPoNumber(String relatedPoNumber) { this.relatedPoNumber = relatedPoNumber; }
}