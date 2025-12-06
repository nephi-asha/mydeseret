package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.dto.AuditLogDto;
import com.mydeseret.mydeseret.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@Tag(name = "Audit Logs", description = "Track history of changes")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Operation(summary = "Get Change History", description = "Returns all versions of a specific record.")
    @GetMapping("/{entity}/{id}")
    @PreAuthorize("hasAuthority('LOG_READ')")
    public ResponseEntity<List<AuditLogDto>> getHistory(
            @PathVariable String entity,
            @PathVariable Long id) {
        
        return ResponseEntity.ok(auditService.getEntityHistory(entity, id));
    }
}