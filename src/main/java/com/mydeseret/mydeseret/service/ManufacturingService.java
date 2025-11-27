package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.BluePrintRequestDto;
import com.mydeseret.mydeseret.dto.BuildRequestDto;
import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.model.enums.StockReason;
import com.mydeseret.mydeseret.repository.*;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManufacturingService {

    @Autowired private BluePrintRepository bluePrintRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private InventoryService inventoryService;

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional
    public BluePrint createBluePrint(BluePrintRequestDto request) {
        
        BluePrint bp = new BluePrint();
        bp.setName(request.getName());
        bp.setOutputQuantity(request.getOutputQuantity());
        
        Item output_item = itemRepository.findById(request.getOutputItemId())
            .orElseThrow(() -> new RuntimeException("Output Item not found"));
        bp.setOutputItem(output_item);

        for (BluePrintRequestDto.ComponentDto compDto : request.getComponents()) {
            Item inputItem = itemRepository.findById(compDto.getInputItemId())
                .orElseThrow(() -> new RuntimeException("Input Item not found: " + compDto.getInputItemId()));

            BluePrintItem bpItem = new BluePrintItem();
            bpItem.setBluePrint(bp);
            bpItem.setInputItem(inputItem);
            bpItem.setQuantityNeeded(compDto.getQuantity());
            
            bp.getComponents().add(bpItem);
        }

        return bluePrintRepository.save(bp);
    }

    @Transactional
    public String buildProduct(Long BluePrintId, int batchesToBuild) {
        BluePrint bp = bluePrintRepository.findById(BluePrintId)
            .orElseThrow(() -> new RuntimeException("BluePrint not found"));

        for (BluePrintItem component : bp.getComponents()) {
            int totalNeeded = component.getQuantityNeeded() * batchesToBuild;
            
            // Because InventoryService checks negative, pls remember that it'll fail if stock is too low
            inventoryService.adjustStock(
                component.getInputItem().getItemId(),
                -totalNeeded, // Negative number removes stock
                StockReason.MANUFACTURING_USE,
                "BUILD-" + bp.getId(),
                "Used to build " + batchesToBuild + " x " + bp.getName()
            );
        }

        //PRODUCE Finished Good
        int totalOutput = bp.getOutputQuantity() * batchesToBuild;
        
        inventoryService.adjustStock(
            bp.getOutputItem().getItemId(),
            totalOutput, // Positive number adds stock
            StockReason.MANUFACTURING_OUTPUT,
            "BUILD-" + bp.getId(),
            "Produced via Manufacturing"
        );

        return "Successfully built " + totalOutput + " " + bp.getOutputItem().getName();
    }

    @Transactional
    public String buildProduct(Long blueprintId, BuildRequestDto request) {
        BluePrint bp = bluePrintRepository.findById(blueprintId)
            .orElseThrow(() -> new RuntimeException("BluePrint not found"));

        int batches = request.getQuantity();

        // This will be a helper that will help creat a list of actuals to a Map for easy lookup (ItemId -> Quantity)
        Map<Long, Integer> actualsMap = Map.of();
        if (request.getActuals() != null) {
            actualsMap = request.getActuals().stream()
                .collect(Collectors.toMap(BuildRequestDto.ActualUsageDto::getInputItemId, 
                                          BuildRequestDto.ActualUsageDto::getActualQuantity));
        }

        // CONSUME MATERIALS
        for (BluePrintItem component : bp.getComponents()) {
            Long itemId = component.getInputItem().getItemId();
            
            // Standard Calculation
            int standardNeeded = component.getQuantityNeeded() * batches;
            
            // Did the user specify actual usage? If not, assume standard.
            int actualUsed = actualsMap.getOrDefault(itemId, standardNeeded);

            // Calculate Variance (Waste)
            int waste = actualUsed - standardNeeded;

            // Deducts Standard Amount (Cost of Good)
            if (standardNeeded > 0) {
                inventoryService.adjustStock(
                    itemId,
                    -standardNeeded, 
                    StockReason.MANUFACTURING_USE,
                    "BUILD-" + bp.getId(),
                    "Standard usage for " + batches + " x " + bp.getName()
                );
            }

            // Handle Variance
            if (waste > 0) {
                // We used MORE than needed -> Log as WASTE 
                inventoryService.adjustStock(
                    itemId,
                    -waste, 
                    StockReason.PRODUCTION_WASTE,
                    "BUILD-" + bp.getId(),
                    "Over-consumption / Waste detected"
                );
            } else if (waste < 0) {
                // We used LESS than needed -> We assume the efficiency is valid, 
                // but we need to correct the inventory because we deducted too much.
                // we removed 100, but we only used 90. So we add back 10.
                inventoryService.adjustStock(
                    itemId,
                    Math.abs(waste), 
                    StockReason.ADJUSTMENT,
                    "BUILD-" + bp.getId(),
                    "Efficiency gain (Used less than recipe)"
                );
            }
        }

        // PRODUCE FINISHED GOODS
        int totalOutput = bp.getOutputQuantity() * batches;
        
        inventoryService.adjustStock(
            bp.getOutputItem().getItemId(),
            totalOutput, 
            StockReason.MANUFACTURING_OUTPUT,
            "BUILD-" + bp.getId(),
            "Produced " + totalOutput + " units"
        );

        return "Successfully built " + totalOutput + " " + bp.getOutputItem().getName();
    }
}