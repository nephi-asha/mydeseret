package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.PaymentMethod;
import com.mydeseret.mydeseret.model.enums.SaleStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleResponseDto {
    private Long id;
    private String receiptNumber;
    private String customerName;
    private SaleStatus status;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private LocalDateTime saleDate;
    private List<SaleItemDto> items;

    public static class SaleItemDto {
        private String itemName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal subTotal;

        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getSubTotal() { return subTotal; }
        public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate = saleDate; }
    public List<SaleItemDto> getItems() { return items; }
    public void setItems(List<SaleItemDto> items) { this.items = items; }
}