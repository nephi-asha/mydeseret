package com.mydeseret.mydeseret.dto;

// import lombok.Data;
import java.util.List;

// @Data
public class BluePrintRequestDto {
    private String name;
    private Long output_item_id;
    private int outputQuantity = 1;
    private List<ComponentDto> components;

    
    // @Data
    public static class ComponentDto {
        private Long input_item_id;
        private int quantity;

        public Long getInputItemId() {
            return input_item_id;
        }
        public void setInputItemId(Long input_item_id) {
            this.input_item_id = input_item_id;
        }
        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        
    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }



    public Long getOutputItemId() {
        return output_item_id;
    }



    public void setOutputItemId(Long output_item_id) {
        this.output_item_id = output_item_id;
    }



    public int getOutputQuantity() {
        return outputQuantity;
    }



    public void setOutputQuantity(int outputQuantity) {
        this.outputQuantity = outputQuantity;
    }



    public List<ComponentDto> getComponents() {
        return components;
    }



    public void setComponents(List<ComponentDto> components) {
        this.components = components;
    }
}