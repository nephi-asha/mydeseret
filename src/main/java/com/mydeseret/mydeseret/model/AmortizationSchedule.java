package com.mydeseret.mydeseret.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "amortization_schedules")
public class AmortizationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(nullable = false)
    private String name; // Example would "Our Bakery Rent 2025"

    private BigDecimal totalAmount; // 1200000

    private int totalMonths; // 6

    private LocalDate startDate;
    private LocalDate endDate;

    // This links to the 12 individual expense records
    @OneToMany(mappedBy = "amortizationSchedule", cascade = CascadeType.ALL)
    private List<Expense> generatedExpenses = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public int getTotalMonths() { return totalMonths; }
    public void setTotalMonths(int totalMonths) { this.totalMonths = totalMonths; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public List<Expense> getGeneratedExpenses() { return generatedExpenses; }
    public void setGeneratedExpenses(List<Expense> generatedExpenses) { this.generatedExpenses = generatedExpenses; }
}