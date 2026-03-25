package com.ordermanagement.productservice.controller;

import com.ordermanagement.productservice.dto.InventoryResponse;
import com.ordermanagement.productservice.dto.InventoryDeductResponse;
import com.ordermanagement.productservice.services.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Inventory API", description = "Operations for managing stock")
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

     public InventoryController(InventoryService inventoryService){
        this.inventoryService=inventoryService;
    }
    @Operation(summary = "Deduct Stock", description = "Reduces product stock if available")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock deducted"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/deduct")
    public ResponseEntity<InventoryDeductResponse> deductStock(@RequestParam Long productId,
                                                               @RequestParam Integer quantity, @RequestHeader(value = "x-conversation-id", required = false)
                    String conversationId) {

        InventoryResponse updatedInventory= inventoryService.deductStock(productId, quantity);
        InventoryDeductResponse response = new InventoryDeductResponse(
                "Stock deducted successfully",
                productId,
                updatedInventory.getAvailableQty()
        );

        return ResponseEntity.ok(response);


    }
    @Operation(summary = "Restore Stock", description = "Adds stock back to product")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock restored"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/restore")
    public ResponseEntity<InventoryDeductResponse> restoreStock(@RequestParam Long productId,
                                                                @RequestParam Integer quantity, @RequestHeader(value = "x-conversation-id", required = false)
                                              String conversationId) {

        InventoryResponse updatedInventory= inventoryService.restoreStock(productId, quantity);
        InventoryDeductResponse response = new InventoryDeductResponse(
                "Stock restored successfully",
                productId,
                updatedInventory.getAvailableQty()
        );

        return ResponseEntity.ok(response);


    }
}


