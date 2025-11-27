package com.mydeseret.mydeseret.dto;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseOrderRequestDto {
    private Long supplierId;
    private List<PoItemDto> items;

    public static class PoItemDto {
        private Long item_id;
        private int quantity;
        private BigDecimal unitCost;
        
        public Long getItemId() { return item_id; }
        public void setItemId(Long item_id) { this.item_id = item_id; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public BigDecimal getUnitCost() { return unitCost; }
        public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    
    public List<PoItemDto> getItems() { return items; }
    public void setItems(List<PoItemDto> items) { this.items = items; }
}