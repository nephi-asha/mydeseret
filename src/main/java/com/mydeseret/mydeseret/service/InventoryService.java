package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.model.Item;
import com.mydeseret.mydeseret.model.StockMove;
import com.mydeseret.mydeseret.model.enums.StockReason;
import com.mydeseret.mydeseret.repository.ItemRepository;
import com.mydeseret.mydeseret.repository.StockMoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final ItemRepository itemRepository;
    private final StockMoveRepository stockMoveRepository;
    private final com.mydeseret.mydeseret.service.messaging.StockAlertProducer stockAlertProducer;

    @Autowired
    public InventoryService(ItemRepository itemRepository, StockMoveRepository stockMoveRepository,
            com.mydeseret.mydeseret.service.messaging.StockAlertProducer stockAlertProducer) {
        this.itemRepository = itemRepository;
        this.stockMoveRepository = stockMoveRepository;
        this.stockAlertProducer = stockAlertProducer;
    }

    /**
     * The ONLY way to change stock.
     * 
     * @param item_id         The item to change.
     * @param quantity_change Positive to add, Negative to remove.
     * @param reason          Why is this happening?
     * @param reference_id    (Optional) "PO-101", "SALE-500", "MANU-20"
     * @param notes           (Optional) Note description.
     */

    @Transactional
    public StockMove adjustStock(Long item_id, int quantity_change, StockReason reason, String reference_id,
            String notes) {
        Item item = itemRepository.findById(item_id)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + item_id));

        int newBalance = item.getQuantityOnHand() + quantity_change;

        // Prevent Negative Stock - Very Strict
        // A business can only request for it to be removed if they want to allow
        // overselling
        if (newBalance < 0) {
            throw new RuntimeException("Insufficient stock for Item: " + item.getName() +
                    ". Current: " + item.getQuantityOnHand() +
                    ", Requested Reduction: " + Math.abs(quantity_change));
        }

        // Update Item Cache
        item.setQuantityOnHand(newBalance);
        itemRepository.save(item);

        // Creates the Audit Log - Immutable
        StockMove move = new StockMove();
        move.setItem(item);
        move.setQuantityChange(quantity_change);
        move.setReason(reason);
        move.setReferenceId(reference_id);
        move.setNotes(notes);

        // Check for Low Stock
        if (newBalance <= item.getReorderPoint()) {
            String currentUserEmail = org.springframework.security.core.context.SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            stockAlertProducer.sendLowStockAlert(currentUserEmail, item.getName(), newBalance);
        }

        return stockMoveRepository.save(move);
    }
}
