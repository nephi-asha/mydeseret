package com.mydeseret.mydeseret.model.enums;

public enum StockReason {
    INITIAL_BALANCE,
    RESTOCK,
    SALE,
    MANUFACTURING_USE,    // Standard Recipe Amount
    MANUFACTURING_OUTPUT, // Finished Goods
    PRODUCTION_WASTE,     // Extra material used (Variance)
    DAMAGE,
    RETURN,
    ADJUSTMENT
}