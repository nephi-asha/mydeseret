package com.mydeseret.mydeseret.dto;

import jakarta.validation.constraints.NotNull;
// import lombok.Data;

// @Data
public class CategoryRequestDto {
    @NotNull(message = "Category name is required")
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}