package com.ordermanagement.productservice.dto;

import lombok.Data;

@Data

public class InventoryResponse {
    private Long productId;
    private Integer availableQty;
}
