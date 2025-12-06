package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.RequisitionRequestDto;
import com.mydeseret.mydeseret.dto.RequisitionResponseDto;
import com.mydeseret.mydeseret.service.RequisitionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/requisitions")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Requisitions", description = "Internal stock requests")
public class RequisitionController {

    @Autowired
    private RequisitionService requisitionService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('REQUISITION_CREATE')")
    public ResponseEntity<RequisitionResponseDto> create(@Valid @RequestBody RequisitionRequestDto request) {
        return ResponseEntity.ok(requisitionService.createRequisition(request));
    }

    @PutMapping("/{id}/approve")
    @PostMapping("/{id}/approve") 
    @PreAuthorize("hasAuthority('REQUISITION_APPROVE')")
    public ResponseEntity<RequisitionResponseDto> approve(@PathVariable Long id) {
        return ResponseEntity.ok(requisitionService.approveRequisition(id));
    }

    @PutMapping("/{id}/reject")
    @PostMapping("/{id}/reject") 
    @PreAuthorize("hasAuthority('REQUISITION_APPROVE')")
    public ResponseEntity<RequisitionResponseDto> reject(@PathVariable Long id, @RequestParam String reason) {
        return ResponseEntity.ok(requisitionService.rejectRequisition(id, reason));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('REQUISITION_READ')")
    public ResponseEntity<Page<RequisitionResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(requisitionService.getAllRequisitions(pageable));
    }
}