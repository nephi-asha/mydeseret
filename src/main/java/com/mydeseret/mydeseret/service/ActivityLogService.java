package com.mydeseret.mydeseret.service;

import com.mydeseret.mydeseret.model.ActivityLog;
import com.mydeseret.mydeseret.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Async
    public void logActivity(UUID userId, String action, String details, String ipAddress) {
        try {
            ActivityLog log = new ActivityLog(userId, action, details, ipAddress);
            activityLogRepository.save(log);
        } catch (Exception e) {
            // Logging should not break the main flow
            System.err.println("Failed to save activity log: " + e.getMessage());
        }
    }
}
