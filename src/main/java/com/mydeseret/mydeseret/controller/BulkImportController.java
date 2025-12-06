package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.ItemRequestDto;
import com.mydeseret.mydeseret.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/import")
@Tag(name = "Bulk Import", description = "Import data from CSV files")
public class BulkImportController {

    @Autowired
    private ItemService itemService;

    @Operation(summary = "Import Items from CSV", description = "CSV Format: name,description,sku,costPrice,sellingPrice,quantityOnHand")
    @PostMapping(value = "/items", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ITEM_CREATE')")
    public ResponseEntity<String> importItems(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        List<String> errors = new ArrayList<>();
        int successCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length < 6) {
                    errors.add("Invalid row format: " + line);
                    continue;
                }

                try {
                    ItemRequestDto item = new ItemRequestDto();
                    item.setName(data[0].trim());
                    item.setDescription(data[1].trim());
                    item.setSku(data[2].trim());
                    item.setCostPrice(new BigDecimal(data[3].trim()));
                    item.setSellingPrice(new BigDecimal(data[4].trim()));
                    item.setQuantityOnHand(Integer.parseInt(data[5].trim()));
                    // Default values
                    item.setUnitOfMeasure("UNIT");
                    item.setReorderPoint(10);

                    itemService.createItem(item);
                    successCount++;
                } catch (Exception e) {
                    errors.add("Error processing row '" + line + "': " + e.getMessage());
                }
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to process file: " + e.getMessage());
        }

        String message = "Import completed. Successfully imported: " + successCount + ". Errors: " + errors.size();
        if (!errors.isEmpty()) {
            message += "\nErrors:\n" + String.join("\n", errors);
        }

        return ResponseEntity.ok(message);
    }
}
