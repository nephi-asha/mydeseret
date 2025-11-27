package com.mydeseret.mydeseret.dto;

import com.mydeseret.mydeseret.model.enums.PaymentMethod;
import java.util.List;

public class SaleRequestDto {
    private Long customerId;
    private PaymentMethod paymentMethod;
    private List<SaleItemDto> items;

    public static class SaleItemDto {
        private Long item_id;
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