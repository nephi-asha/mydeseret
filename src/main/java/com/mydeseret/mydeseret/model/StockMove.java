package com.mydeseret.mydeseret.model;

import jakarta.persistence.*;
// import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.mydeseret.mydeseret.model.enums.StockReason;

@Entity
@Table(name = "stock_moves")
// @Data
public class StockMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_move_id")    
    private Long stockMoveId;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @NotNull
    @Column(name = "quantity_change")
    private int quantityChange; // Positive for Restock, Negative for Usage

    @NotNull
    @Column(name = "reason")
    @Enumerated(EnumType.STRING)
    private StockReason reason; 

    @Column(name = "reference_id")
    private String reference_id; // This will link to "PurchaseOrder #101" or "Sale #505" probably. I'll keep it here for now

    @Column(name = "notes")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime created_at = LocalDateTime.now();

    public Long getMoveId() {
        return stockMoveId;
    }

    public void setMoveId(Long stockMoveId) {
        this.stockMoveId = stockMoveId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }

    public StockReason getReason() {
        return reason;
    }

    public void setReason(StockReason reason) {
        this.reason = reason;
    }

    public String getReferenceId() {
        return reference_id;
    }

    public void setReferenceId(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    

}