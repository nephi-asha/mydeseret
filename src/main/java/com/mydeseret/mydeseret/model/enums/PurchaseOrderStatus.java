package com.mydeseret.mydeseret.model.enums;

public enum PurchaseOrderStatus {
    PENDING,    // A Draft
    ORDERED,    // Sent to Vendor
    RECEIVED,   // Goods arrived (Inventory Updated)
    PAID,       // Paid for (Expense Created)
    CANCELLED
}
