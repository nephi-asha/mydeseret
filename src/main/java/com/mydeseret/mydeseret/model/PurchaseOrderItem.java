package com.mydeseret.mydeseret.model;

import jakarta.persistence.*;
// import lombok.Data;
import java.math.BigDecimal;

import org.hibernate.envers.Audited;

// @Data
@Entity
@Table(name = "purchase_order_items")
@Audited
public class PurchaseOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_order_item_id")    
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;
    
    @Column(name = "unit_cost")
    private BigDecimal unitCost; // Cost at the moment of purchase

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getUnitCost() {
        return unitCost;
    }
    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    
}