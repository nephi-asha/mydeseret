package com.mydeseret.mydeseret.dto;

// import lombok.Data;
import java.math.BigDecimal;

// @Data
public class ItemResponseDto {
    private Long item_id;
    private String name;
    private String sku;
    private String description;
    private String unit_of_measure;
    private BigDecimal cost_price;
    private BigDecimal selling_price;
    private int quantity_on_hand; // Cannot be changed
    private String categoryName;
    private Long category_id;
    private String imageKey;
    private String imageUrl;

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    
    public Long getItemId() {
        return item_id;
    }
    public void setItemId(Long item_id) {
        this.item_id = item_id;
    }
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
    public int getQuantityOnHand() {
        return quantity_on_hand;
    }
    public void setQuantityOnHand(int quantity_on_hand) {
        this.quantity_on_hand = quantity_on_hand;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public Long getCategoryId() {
        return category_id;
    }
    public void setCategoryId(Long category_id) {
        this.category_id = category_id;
    }
    
}