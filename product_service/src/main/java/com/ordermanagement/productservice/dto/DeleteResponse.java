package com.ordermanagement.productservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object returned after deleting a resource")
public class DeleteResponse {

    @Schema(description = "Message indicating the result of the delete operation", example = "Product deleted successfully")
    private String message;

    @Schema(description = "ID of the deleted resource", example = "101")
    private Long id;

    @Schema(description = "Status of the deleted resource", example = "INACTIVE")
    private String status;
}