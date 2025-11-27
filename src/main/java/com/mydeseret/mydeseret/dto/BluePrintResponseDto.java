package com.mydeseret.mydeseret.dto;

import java.util.List;

public class BluePrintResponseDto {
    private Long id;
    private String name;
    private String outputItemName; // Just the name, not the full Item object
    private int outputQuantity;
    private List<ComponentDto> components;

    public static class ComponentDto {
        private String inputItemName;
        private int quantityNeeded;

        public String getInputItemName() { return inputItemName; }
        public void setInputItemName(String inputItemName) { this.inputItemName = inputItemName; }
        public int getQuantityNeeded() { return quantityNeeded; }
        public void setQuantityNeeded(int quantityNeeded) { this.quantityNeeded = quantityNeeded; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOutputItemName() { return outputItemName; }
    public void setOutputItemName(String outputItemName) { this.outputItemName = outputItemName; }
    public int getOutputQuantity() { return outputQuantity; }
    public void setOutputQuantity(int outputQuantity) { this.outputQuantity = outputQuantity; }
    public List<ComponentDto> getComponents() { return components; }
    public void setComponents(List<ComponentDto> components) { this.components = components; }
}