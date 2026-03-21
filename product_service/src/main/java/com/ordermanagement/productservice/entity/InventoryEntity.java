package com.ordermanagement.productservice.entity;

import lombok.Data;

@Data
public class InventoryEntity {
    private Long productId;
    private Integer availableQty;
}
