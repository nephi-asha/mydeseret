package com.mydeseret.mydeseret.model;

import com.mydeseret.mydeseret.model.enums.PayRollStatus;
import jakarta.persistence.*;
// import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payrolls")
// @Data
public class PayRoll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id")    
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "pay_period")
    private LocalDate payPeriod; // Like 2025-11-01 (November Salary)

    @Column(name = "base_salary")
    private BigDecimal baseSalary;
    
    private BigDecimal bonuses = BigDecimal.ZERO;
    private BigDecimal deductions = BigDecimal.ZERO;
    
    // Calculated: Base + Bonus - Deduction
    private BigDecimal netPay; 

    @Enumerated(EnumType.STRING)
    private PayRollStatus status = PayRollStatus.PENDING;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(LocalDate payPeriod) {
        this.payPeriod = payPeriod;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getBonuses() {
        return bonuses;
    }

    public void setBonuses(BigDecimal bonuses) {
        this.bonuses = bonuses;
    }

    public BigDecimal getDeductions() {
        return deductions;
    }

    public void setDeductions(BigDecimal deductions) {
        this.deductions = deductions;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }

    public PayRollStatus getStatus() {
        return status;
    }

    public void setStatus(PayRollStatus status) {
        this.status = status;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}