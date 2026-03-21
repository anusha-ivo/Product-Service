package com.ordermanagement.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object returned after updating a product")
public class ProductUpdate {

    @Schema(description = "Message indicating the result of the update operation", example = "Product updated successfully")
    private String message;

    @Schema(description = "ID of the updated product", example = "101")
    private Long productId;

    @Schema(description = "Current status of the product after update", example = "ACTIVE")
    private String status;

    @Schema(description = "Detailed product information after update")
    private ProductResponse product;
}