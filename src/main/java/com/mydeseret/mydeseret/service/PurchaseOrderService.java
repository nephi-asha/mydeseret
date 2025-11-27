package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.PurchaseOrderRequestDto;
import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.model.enums.PurchaseOrderStatus;
import com.mydeseret.mydeseret.model.enums.StockReason;
import com.mydeseret.mydeseret.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class PurchaseOrderService {

    @Autowired private PurchaseOrderRepository poRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private SupplierRepository supplierRepository;
    @Autowired private InventoryService inventoryService;

    @Transactional
    public PurchaseOrder createPO(PurchaseOrderRequestDto request) {
        PurchaseOrder po = new PurchaseOrder();
        
        po.setPoNumber("PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        //Links Supplier
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        po.setSupplier(supplier);
        
        po.setStatus(PurchaseOrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseOrderRequestDto.PoItemDto itemDto : request.getItems()) {
            Item item = itemRepository.findById(itemDto.getItemId()).orElseThrow();
            
            PurchaseOrderItem poItem = new PurchaseOrderItem();
            poItem.setPurchaseOrder(po);
            poItem.setItem(item);
            poItem.setQuantity(itemDto.getQuantity());
            poItem.setUnitCost(itemDto.getUnitCost());
            
            po.getItems().add(poItem);
            
            total = total.add(itemDto.getUnitCost().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }
        po.setTotalCost(total);
        return poRepository.save(po);
    }

    // RECEIVE
    @Transactional
    public PurchaseOrder receiveGoods(Long poId) {
        PurchaseOrder po = poRepository.findById(poId).orElseThrow();
        
        if (po.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new RuntimeException("PO already received!");
        }

        for (PurchaseOrderItem item : po.getItems()) {
            inventoryService.adjustStock(
                item.getItem().getItemId(),
                item.getQuantity(),
                StockReason.RESTOCK,
                po.getPoNumber(),
                "Received from " + po.getSupplier().getName()
            );
        }

        po.setStatus(PurchaseOrderStatus.RECEIVED);
        return poRepository.save(po);
    }

    // PAY
    @Transactional
    public PurchaseOrder payVendor(Long poId) {
        PurchaseOrder po = poRepository.findById(poId).orElseThrow();
        
        if (po.getStatus() != PurchaseOrderStatus.RECEIVED) {
            throw new RuntimeException("You should receive goods before paying!");
        }

        Expense expense = new Expense();
        expense.setAmount(po.getTotalCost());
        expense.setDescription("Payment for " + po.getPoNumber() + " to " + po.getSupplier().getName());
        expense.setPurchaseOrder(po);
        expense.setDate(LocalDate.now());
        
        expenseRepository.save(expense);

        po.setStatus(PurchaseOrderStatus.PAID);
        return poRepository.save(po);
    }
}