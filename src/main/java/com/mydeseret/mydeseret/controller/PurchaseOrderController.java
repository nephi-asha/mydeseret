package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.PurchaseOrderRequestDto;
import com.mydeseret.mydeseret.model.PurchaseOrder;
import com.mydeseret.mydeseret.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Purchase Orders", description = "Manage supplier orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService poService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_CREATE')")
    public ResponseEntity<PurchaseOrder> createPO(@RequestBody PurchaseOrderRequestDto request) {
        return ResponseEntity.ok(poService.createPO(request));
    }

    @PutMapping("/{id}/receive")
    @PostMapping("/{id}/receive") 
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_UPDATE')")
    public ResponseEntity<PurchaseOrder> receiveGoods(@PathVariable Long id) {
        return ResponseEntity.ok(poService.receiveGoods(id));
    }

    @PutMapping("/{id}/pay")
    @PostMapping("/{id}/pay") 
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_PAY')")
    public ResponseEntity<PurchaseOrder> payVendor(@PathVariable Long id) {
        return ResponseEntity.ok(poService.payVendor(id));
    }
}