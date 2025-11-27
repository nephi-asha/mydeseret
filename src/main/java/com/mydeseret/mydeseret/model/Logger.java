package com.mydeseret.mydeseret.model;

import java.time.LocalDate;

import com.mydeseret.mydeseret.model.enums.LogLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
// import lombok.Data;

@Entity
@Table(name = "logs", schema = "public")
// @Data
public class Logger {
    @Id
    @GeneratedValue
    @Column(name = "log_id")    
    private Long logId;

    @NotNull
    @Column(name = "level")
    private LogLevel level = LogLevel.INFO;

    @NotNull
    @Column(name = "message")
    private String message;

    @NotNull
    @Column(name = "created_at")
    private LocalDate createdDate;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    
}
