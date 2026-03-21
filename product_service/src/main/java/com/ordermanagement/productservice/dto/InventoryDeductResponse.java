package com.ordermanagement.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Response returned after deducting inventory for a product")
public class InventoryDeductResponse {
    @Schema(description = "Message indicating the result of the operation", example = "Stock deducted successfully")
    private String message;

    @Schema(description = "ID of the product whose inventory was updated", example = "101")
    private Long productId;

    @Schema(description = "Updated available quantity after deduction", example = "45")
    private Integer availableQty;
}
