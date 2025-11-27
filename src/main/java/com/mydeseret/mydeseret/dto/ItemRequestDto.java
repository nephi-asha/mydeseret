package com.mydeseret.mydeseret.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
// import lombok.Data;
import java.math.BigDecimal;

// @Data
public class ItemRequestDto {
    @NotNull(message = "Item name is required")
    private String name;

    @NotNull(message = "SKU is required")
    private String sku;

    private String description;

    @NotNull(message = "Unit of Measure is required (e.g., PCS, KG, LITER)")
    private String unit_of_measure;

    @PositiveOrZero(message = "Cost price must be positive")
    private BigDecimal cost_price;

    @PositiveOrZero(message = "Selling price must be positive")
    private BigDecimal selling_price;

    @PositiveOrZero
    private int reorderPoint;

    // Optional: Only If a business want to organize items by category
    private Long category_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitOfMeasure() {
        return unit_of_measure;
    }

    public void setUnitOfMeasure(String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
    }

    public BigDecimal getCostPrice() {
        return cost_price;
    }

    public void setCostPrice(BigDecimal cost_price) {
        this.cost_price = cost_price;
    }

    public BigDecimal getSellingPrice() {
        return selling_price;
    }

    public void setSellingPrice(BigDecimal selling_price) {
        this.selling_price = selling_price;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Long getCategoryId() {
        return category_id;
    }

    public void setCategoryId(Long category_id) {
        this.category_id = category_id;
    }

    
}