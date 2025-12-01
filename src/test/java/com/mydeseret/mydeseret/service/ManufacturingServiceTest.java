package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.dto.BuildRequestDto;
import com.mydeseret.mydeseret.model.*;
import com.mydeseret.mydeseret.model.enums.StockReason;
import com.mydeseret.mydeseret.repository.BluePrintRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Enables Mockito
public class ManufacturingServiceTest {

    @Mock private BluePrintRepository bluePrintRepository;
    @Mock private InventoryService inventoryService;

    @InjectMocks
    private ManufacturingService manufacturingService;

    @Test
    void buildProduct_ShouldCalculateVarianceCorrectly() {

        Item flour = new Item(); flour.setItemId(1L); flour.setName("Flour");
        Item bread = new Item(); bread.setItemId(2L); bread.setName("Bread");

        BluePrint bp = new BluePrint();
        bp.setId(100L);
        bp.setName("Standard Loaf");
        bp.setOutputItem(bread);
        bp.setOutputQuantity(1);

        BluePrintItem bpItem = new BluePrintItem();
        bpItem.setInputItem(flour);
        bpItem.setQuantityNeeded(10);
        bp.setComponents(List.of(bpItem));

        when(bluePrintRepository.findById(100L)).thenReturn(Optional.of(bp));

        // Create Request: Build 5 Breads, but use 60 Flour (10 extra)
        BuildRequestDto request = new BuildRequestDto();
        request.setQuantity(5); // Standard need = 5 * 10 = 50
        
        BuildRequestDto.ActualUsageDto actualUsage = new BuildRequestDto.ActualUsageDto();
        actualUsage.setInputItemId(1L);
        actualUsage.setActualQuantity(60); // User says they used 60
        request.setActuals(List.of(actualUsage));

        // EXECUTE (The "When")
        manufacturingService.buildProduct(100L, request);

        // VERIFY (The "Then")
        
        // Verify Standard Usage (50 units removed)
        verify(inventoryService).adjustStock(
            eq(1L), 
            eq(-50), 
            eq(StockReason.MANUFACTURING_USE), 
            anyString(), 
            anyString()
        );

        // Verify Variance/Waste (10 units removed)
        verify(inventoryService).adjustStock(
            eq(1L), 
            eq(-10), 
            eq(StockReason.PRODUCTION_WASTE), 
            anyString(), 
            contains("Over-consumption")
        );

        // Verify Production (5 Breads added)
        verify(inventoryService).adjustStock(
            eq(2L), 
            eq(5), 
            eq(StockReason.MANUFACTURING_OUTPUT), 
            anyString(), 
            anyString()
        );
    }
}