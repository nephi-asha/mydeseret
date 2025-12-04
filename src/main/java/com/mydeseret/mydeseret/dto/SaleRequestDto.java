package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(
    description = "Payload for creating a new Sale (Point of Sale Transaction)",
    example = """
        {
          "customerId": 52,
          "paymentMethod": "CASH",
          "items": [
            { "itemId": 10, "quantity": 2 },
            { "itemId": 15, "quantity": 1 }
          ]
        }
    """
)
public class SaleRequestDto {

    @Schema(
        description = "ID of the Customer buying the items. If omitted/null, treated as a 'Guest' sale.", 
        example = "52",
        nullable = true
    )
    private Long customerId;

    @Schema(
        description = "How the customer paid.", 
        example = "CASH"
    )
    @NotNull(message = "Payment Method is required")
    private PaymentMethod paymentMethod;

    @Schema(
        description = "List of products to sell. Must contain at least one item."
    )
    @NotEmpty(message = "Sale must have at least one item")
    private List<SaleItemDto> items;
    @Schema(description = "Line item details for the sale")
    public static class SaleItemDto {
        
        @Schema(description = "ID of the Inventory Item to sell", example = "10")
        @NotNull(message = "Item ID is required")
        private Long item_id; 

        @Schema(description = "Quantity to sell", example = "2", minimum = "1")
        @NotNull(message = "Quantity is required")
        private int quantity;

        public Long getItemId() { return item_id; }
        public void setItemId(Long item_id) { this.item_id = item_id; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public List<SaleItemDto> getItems() { return items; }
    public void setItems(List<SaleItemDto> items) { this.items = items; }
}