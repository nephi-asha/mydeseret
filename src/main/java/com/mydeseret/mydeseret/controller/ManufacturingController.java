package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.BluePrintRequestDto;
import com.mydeseret.mydeseret.model.BluePrint;
import com.mydeseret.mydeseret.service.ManufacturingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.mydeseret.mydeseret.dto.BluePrintResponseDto;
import com.mydeseret.mydeseret.dto.BuildRequestDto;
import com.mydeseret.mydeseret.mapper.BluePrintMapper;

@RestController
@RequestMapping("/api/v1/manufacturing")
public class ManufacturingController {

    @Autowired private ManufacturingService manufacturingService;
    @Autowired private BluePrintMapper bluePrintMapper;

    @PostMapping("/BluePrints")
    @PreAuthorize("hasAuthority('BLUEPRINT_CREATE')")
    public ResponseEntity<BluePrintResponseDto> createBluePrint(@RequestBody BluePrintRequestDto request) {
        BluePrint savedBlueprint = manufacturingService.createBluePrint(request);
        return ResponseEntity.ok(bluePrintMapper.toResponseDto(savedBlueprint));
    }

    // POST /api/v1/manufacturing/build/{BluePrintId}?quantity=1
    // @PostMapping("/build/{BluePrintId}")
    // @PreAuthorize("hasAuthority('MANUFACTURING_BUILD')")
    // public ResponseEntity<String> buildProduct(
    //         @PathVariable Long BluePrintId, 
    //         @RequestParam(defaultValue = "1") int quantity) {
        
    //     return ResponseEntity.ok(manufacturingService.buildProduct(BluePrintId, quantity));
    // }
    // POST /api/v1/manufacturing/build/{blueprintId}
    @PostMapping("/build/{blueprintId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_BUILD')")    
    public ResponseEntity<String> buildProduct(
            @PathVariable Long blueprintId, 
            @RequestBody BuildRequestDto request) {
        
        return ResponseEntity.ok(manufacturingService.buildProduct(blueprintId, request));
    }
}