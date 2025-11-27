package com.mydeseret.mydeseret.model;

import jakarta.persistence.*;
// import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "items")
// @Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")    
    private Long itemId;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String sku;

    private String description;

    @NotNull
    @Column(name = "unit_of_measure")
    private String unitOfMeasure; // Usage will be: KG, LITER, PCS

    @Column(name = "cost_price")
    private BigDecimal costPrice = BigDecimal.ZERO; // Average cost to buy this

    @Column(name = "selling_price")
    private BigDecimal sellingPrice = BigDecimal.ZERO; // I'll leave this here for businesses that might want to sell raw items. IDK.

    @Column(name = "quantity_on_hand")
    private int quantityOnHand = 0; // Will be cached for speed

    @Column(name = "reorder_point")
    private int reorderPoint = 10; // Will set this to 10 so it alerts when stock drops below this value


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_at")
    private LocalDate createdDate = LocalDate.now();
    
    @Column(name = "updated_at")
    private LocalDate updatedDate = LocalDate.now();

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long item_id) {
        this.itemId = item_id;
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

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(int quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    
}