package com.mydeseret.mydeseret.model.enums;

public enum RequisitionStatus {
    PENDING,    // Waiting for manager
    APPROVED,   // Manager said yes (Ready to buy)
    REJECTED,   // Manager said no
    ORDERED     // Converted to Purchase Order
}