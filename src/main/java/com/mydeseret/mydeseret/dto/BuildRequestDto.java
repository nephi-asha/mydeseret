package com.mydeseret.mydeseret.dto;

import jakarta.validation.constraints.Min;
import java.util.List;

public class BuildRequestDto {
    
    @Min(1)
    private int quantity; 

    private List<ActualUsageDto> actuals;

    public static class ActualUsageDto {
        private Long inputItemId;
        private int actualQuantity; // The total amount actually consumed

        public Long getInputItemId() { return inputItemId; }
        public void setInputItemId(Long inputItemId) { this.inputItemId = inputItemId; }
        public int getActualQuantity() { return actualQuantity; }
        public void setActualQuantity(int actualQuantity) { this.actualQuantity = actualQuantity; }
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public List<ActualUsageDto> getActuals() { return actuals; }
    public void setActuals(List<ActualUsageDto> actuals) { this.actuals = actuals; }
}