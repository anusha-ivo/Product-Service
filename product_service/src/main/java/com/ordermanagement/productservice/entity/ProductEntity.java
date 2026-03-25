package com.ordermanagement.productservice.entity;



import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductEntity {

    private Long productId;
    private String stockKeepingUnit;
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

