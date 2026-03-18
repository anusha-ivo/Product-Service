package com.ordermanagement.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a product in the inventory system")
public class Product {

    @Schema(description = "Unique identifier for the product", example = "101")
    private Long productId;

    @NotBlank
    @Schema(description = "Unique Stock Keeping Unit (SKU) for the product", example = "SKU-12345", required = true)
    private String stockKeepingUnit;

    @NotBlank
    @Schema(description = "Name of the product", example = "Wireless Mouse", required = true)
    private String name;

    @Schema(description = "Detailed description of the product", example = "Ergonomic wireless mouse with USB receiver")
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(description = "Price of the product", example = "25.99", required = true)
    private BigDecimal price;

    @NotBlank
    @Schema(description = "Currency code for the price (ISO 4217)", example = "USD", required = true)
    private String currency;

    @NotBlank
    @Schema(description = "Current status of the product", example = "ACTIVE", required = true)
    private String status;

    @Schema(description = "Available quantity of the product in stock", example = "50")
    private Integer availableQty;

    @Schema(description = "Timestamp when the product was created", example = "2026-03-18T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the product was last updated", example = "2026-03-18T12:45:30")
    private LocalDateTime updatedAt;
}