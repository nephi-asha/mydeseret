package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.InventoryRequestDto;
import com.mydeseret.mydeseret.dto.StockMoveResponseDto;
import com.mydeseret.mydeseret.mapper.StockMoveMapper;
import com.mydeseret.mydeseret.model.StockMove;
import com.mydeseret.mydeseret.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Inventory Management", description = "Endpoints for Stock Adjustments and Tracking")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private StockMoveMapper stockMoveMapper;

    @io.swagger.v3.oas.annotations.Operation(summary = "Adjust Stock", description = "Manually adjust stock levels for an item (e.g., for damage, shrinkage, or corrections).")
    @PostMapping("/adjust")
    @PreAuthorize("hasAuthority('INVENTORY_ADJUST')")
    public ResponseEntity<StockMoveResponseDto> adjustStock(@Valid @RequestBody InventoryRequestDto request) {
        StockMove move = inventoryService.adjustStock(
                request.getItemId(),
                request.getQuantityChange(),
                request.getReason(),
                request.getReferenceId(),
                request.getNotes());
        return ResponseEntity.ok(stockMoveMapper.toResponseDto(move));
    }
}