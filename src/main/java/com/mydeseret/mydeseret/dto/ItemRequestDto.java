package com.mydeseret.mydeseret.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.PositiveOrZero;
// import lombok.Data;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

// @Data
@Schema(description = "Payload for creating or updating an Inventory Item")
public class ItemRequestDto {
    @Schema(description = "The display name of the item", example = "Premium White Flour")
    @NotNull(message = "Name is required")
    private String name;

    @Schema(description = "Stock Keeping Unit - Unique identifier for tracking", example = "RAW-FLR-001")
    @NotNull(message = "SKU is required")
    private String sku;

    @Schema(description = "Detailed description of the item", example = "High-grade flour suitable for pastries and breads")
    private String description;

    @Schema(description = "The unit of measurement for this item", example = "KG", allowableValues = { "KG", "UNIT",
            "LITRE", "BOX" })
    @NotNull(message = "Unit of Measure is required")
    private String unitOfMeasure;

    @Schema(description = "The cost to purchase or produce one unit", example = "1.50")
    private BigDecimal costPrice;

    @Schema(description = "The price this item is sold at (if applicable)", example = "0.00")
    private BigDecimal sellingPrice;

    @Schema(description = "Threshold for low-stock alerts", example = "50")
    @Min(0)
    private int reorderPoint;

    @Schema(description = "ID of the Category this item belongs to", example = "1")
    private Long categoryId;

    @Schema(description = "Initial quantity on hand", example = "100")
    @Min(0)
    private int quantityOnHand;

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
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }
}