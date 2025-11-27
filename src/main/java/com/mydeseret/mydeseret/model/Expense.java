package com.mydeseret.mydeseret.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    private String description;
    
    private BigDecimal amount;

    @Column(name = "expense_date")
    private LocalDate date = LocalDate.now();

    @OneToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    // Links to the Amortization Schedule
    @ManyToOne
    @JoinColumn(name = "amortization_schedule_id")
    private AmortizationSchedule amortizationSchedule;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }
    
    public AmortizationSchedule getAmortizationSchedule() { return amortizationSchedule; }
    public void setAmortizationSchedule(AmortizationSchedule amortizationSchedule) { 
        this.amortizationSchedule = amortizationSchedule; 
    }
}