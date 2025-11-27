package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.PayRollStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PayrollResponseDto {
    private Long id;
    private String employeeName;
    private LocalDate payPeriod;
    private BigDecimal baseSalary;
    private BigDecimal netPay;
    private PayRollStatus status;
    private LocalDate paymentDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public LocalDate getPayPeriod() { return payPeriod; }
    public void setPayPeriod(LocalDate payPeriod) { this.payPeriod = payPeriod; }
    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }
    public BigDecimal getNetPay() { return netPay; }
    public void setNetPay(BigDecimal netPay) { this.netPay = netPay; }
    public PayRollStatus getStatus() { return status; }
    public void setStatus(PayRollStatus status) { this.status = status; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}