package com.ordermanagement.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents the inventory details of a product")
public class InventoryRequest {

    @NotNull
    @Schema(description = "ID of the product", example = "101", required = true)
    private Long productId;

    @NotNull
    @Min(0)
    @Schema(description = "Available quantity of the product in stock", example = "50", required = true)
    private Integer availableQty;

    @Schema(description = "Timestamp of the last update to inventory", example = "2026-03-18T10:15:30")
    private LocalDateTime updatedAt;
}