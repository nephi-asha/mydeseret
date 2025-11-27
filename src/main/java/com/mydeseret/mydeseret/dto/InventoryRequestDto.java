package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.StockReason;
import jakarta.validation.constraints.NotNull;
// import lombok.Data;

// @Data
public class InventoryRequestDto {
    @NotNull(message = "Item ID is required")
    private Long item_id;

    @NotNull(message = "Quantity change is required")
    private Integer quantity_change;

    @NotNull(message = "Reason is required")
    private StockReason reason;

    private String reference_id; // Like.. "PO-1234"
    private String notes;       //Something like "Received from Supplier ABC"
    
    public Long getItemId() {
        return item_id;
    }
    public void setItemId(Long item_id) {
        this.item_id = item_id;
    }
    public Integer getQuantityChange() {
        return quantity_change;
    }
    public void setQuantityChange(Integer quantity_change) {
        this.quantity_change = quantity_change;
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

    

}