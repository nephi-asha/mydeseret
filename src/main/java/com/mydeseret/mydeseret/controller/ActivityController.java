package com.mydeseret.mydeseret.controller;

import com.mydeseret.mydeseret.model.ActivityLog;
import com.mydeseret.mydeseret.repository.ActivityLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/activity")
@Tag(name = "Activity Logs", description = "View user activity history")
public class ActivityController {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Operation(summary = "Get User Activity", description = "Returns the activity log for a specific user.")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('LOG_READ')")
    public ResponseEntity<List<ActivityLog>> getUserActivity(@PathVariable UUID userId) {
        return ResponseEntity.ok(activityLogRepository.findByUserIdOrderByTimestampDesc(userId));
    }
}
