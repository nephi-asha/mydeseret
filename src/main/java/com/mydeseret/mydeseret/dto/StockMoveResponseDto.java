package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.StockReason;
import java.time.LocalDateTime;

public class StockMoveResponseDto {
    private Long id;
    private String itemName;
    private int quantityChange;
    private StockReason reason;
    private String referenceId;
    private String notes;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantityChange() { return quantityChange; }
    public void setQuantityChange(int quantityChange) { this.quantityChange = quantityChange; }
    public StockReason getReason() { return reason; }
    public void setReason(StockReason reason) { this.reason = reason; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}