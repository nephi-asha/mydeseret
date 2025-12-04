package com.mydeseret.mydeseret.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(
    description = "Payload for creating a Purchase Order (Restocking Inventory)",
    example = """
        {
          "supplierId": 3,
          "items": [
            { "itemId": 101, "quantity": 500, "unitCost": 1.50 }
          ]
        }
    """
)
public class PurchaseOrderRequestDto {

    @Schema(description = "ID of the Supplier we are buying from", example = "3")
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @Schema(description = "List of items to order")
    @NotEmpty(message = "Must order at least one item")
    private List<PoItemDto> items;

    @Schema(description = "Line item for the Purchase Order")
    public static class PoItemDto {
        
        @Schema(description = "ID of the Item (Raw Material) to buy", example = "101")
        @NotNull
        private Long itemId;

        @Schema(description = "Quantity to order", example = "500")
        @NotNull
        private int quantity;

        @Schema(
            description = "Agreed cost per unit for THIS specific order. (May differ from standard cost)", 
            example = "1.50"
        )
        @NotNull
        private BigDecimal unitCost;

        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
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