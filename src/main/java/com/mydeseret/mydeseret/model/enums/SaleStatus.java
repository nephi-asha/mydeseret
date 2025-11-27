package com.mydeseret.mydeseret.model.enums;

public enum SaleStatus {
    PENDING,    // Draft sale
    COMPLETED,  // Paid and stock deducted
    CANCELLED,  // Voided (Stock returned)
    REFUNDED    // Returned by customer
}