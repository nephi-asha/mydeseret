package com.mydeseret.mydeseret.dto;

import java.math.BigDecimal;

public class TopItemDto {
    private String itemName;
    private Long totalQuantity;
    private BigDecimal totalRevenue;

    public TopItemDto(String itemName, Long totalQuantity, BigDecimal totalRevenue) {
        this.itemName = itemName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
    }

    
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
