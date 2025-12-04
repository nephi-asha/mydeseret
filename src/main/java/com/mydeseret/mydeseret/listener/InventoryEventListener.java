package com.mydeseret.mydeseret.listener;

import com.mydeseret.mydeseret.event.SaleCreatedEvent;
import com.mydeseret.mydeseret.model.SaleItem;
import com.mydeseret.mydeseret.model.enums.StockReason;
import com.mydeseret.mydeseret.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventListener {

    @Autowired private InventoryService inventoryService;

    @EventListener 
    public void handleInventoryDeduction(SaleCreatedEvent event) {
        var sale = event.getSale();
        String customerName = sale.getCustomer() != null ? sale.getCustomer().getName() : "Guest";

        for (SaleItem item : sale.getItems()) {
            inventoryService.adjustStock(
                item.getItem().getItemId(),
                -item.getQuantity(),
                StockReason.SALE,
                sale.getReceiptNumber(),
                "Sold to " + customerName
            );
        }
        System.out.println("Inventory deducted for Sale: " + sale.getReceiptNumber());
    }
}