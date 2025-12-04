package com.mydeseret.mydeseret.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.util.List;

@Schema(
    description = "Payload for running a Production Batch (Building products from a Blueprint)",
    example = """
        {
          "quantity": 10,
          "actuals": [
            { "inputItemId": 15, "actualQuantity": 55 }
          ]
        }
    """
)
public class BuildRequestDto {
    
    @Schema(
        description = "Number of finished goods to produce (e.g., 10 Loaves)", 
        example = "10", 
        minimum = "1"
    )
    @Min(value = 1, message = "Must build at least 1 unit")
    private int quantity;

    @Schema(
        description = "Optional: List of actual material usage. If omitted for an item, the system assumes standard recipe amount was used. Use this to report waste or efficiency gains.",
        nullable = true
    )
    private List<ActualUsageDto> actuals;

    // --- Inner Class ---
    @Schema(description = "Overrides the standard recipe quantity for a specific ingredient")
    public static class ActualUsageDto {
        
        @Schema(description = "ID of the Raw Material (Input Item) used", example = "15")
        private Long inputItemId;

        @Schema(
            description = "The TOTAL amount actually consumed for this entire batch. " +
                          "Example: If recipe needs 50kg but you spilled some, enter 55.", 
            example = "55"
        )
        private int actualQuantity;

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