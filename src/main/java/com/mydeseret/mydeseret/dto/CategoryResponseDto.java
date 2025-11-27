package com.mydeseret.mydeseret.dto;

// import lombok.Data;

// @Data
public class CategoryResponseDto {
    private Long category_id;
    private String name;
    private String description;
    
    public Long getCategoryId() {
        return category_id;
    }
    public void setCategoryId(Long category_id) {
        this.category_id = category_id;
    }
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