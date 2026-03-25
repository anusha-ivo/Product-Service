package com.ordermanagement.productservice.services;

import com.ordermanagement.productservice.dto.InventoryRequest;
import com.ordermanagement.productservice.dto.InventoryResponse;
import com.ordermanagement.productservice.entity.InventoryEntity;
import com.ordermanagement.productservice.exceptions.ProductException;
import com.ordermanagement.productservice.repository.InventoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


    @Transactional
    public InventoryResponse deductStock(Long productId, Integer qty) {

        InventoryEntity entity = inventoryRepository
                .deductStock(productId, qty)
                .orElseThrow(() -> new ProductException(
                        "Insufficient stock for product " + productId,
                        HttpStatus.BAD_REQUEST,
                        "INSUFFICIENT_STOCK"
                ));

        return mapToResponse(entity);
    }


    @Transactional
    public InventoryResponse restoreStock(Long productId, Integer qty) {

        InventoryEntity entity = inventoryRepository
                .restoreStock(productId, qty)
                .orElseThrow(() -> new ProductException(
                        "Inventory not found for product " + productId,
                        HttpStatus.NOT_FOUND,
                        "INVENTORY_NOT_FOUND"
                ));

        return mapToResponse(entity);
    }



    private InventoryResponse mapToResponse(InventoryEntity entity) {
        if (entity == null) {
            throw new ProductException(
                    "Inventory entity is null",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "NULL_ENTITY"
            );
        }
        InventoryResponse response = new InventoryResponse();
        response.setProductId(entity.getProductId());
        response.setAvailableQty(entity.getAvailableQty());
        return response;
    }
    }

