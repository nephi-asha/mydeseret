package com.mydeseret.mydeseret.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AuditLogDto {
    private Number revisionId;
    private String changedBy; 
    private LocalDateTime changedAt;
    private String revisionType; // ADD, MOD, DEL
    private Object entityState; 

    public AuditLogDto(Number revisionId, String changedBy, long timestamp, Object revisionType, Object entityState) {
        this.revisionId = revisionId;
        this.changedBy = changedBy;
        this.changedAt = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.revisionType = revisionType.toString();
        this.entityState = entityState;
    }

    public Number getRevisionId() { return revisionId; }
    public String getChangedBy() { return changedBy; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getRevisionType() { return revisionType; }
    public Object getEntityState() { return entityState; }
}