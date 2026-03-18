package com.ordermanagement.productservice.services;

import com.ordermanagement.productservice.dto.Inventory;
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
    public Inventory deductStock(Long productId, Integer qty) {

        int rows = inventoryRepository.deductStock(productId, qty);

        if (rows == 0) {
            throw new ProductException(
                    "Insufficient stock for product " + productId,
                    HttpStatus.BAD_REQUEST,
                    "INSUFFICIENT_STOCK"
            );
        }
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductException(
                        "Inventory not found for product " + productId,
                        HttpStatus.NOT_FOUND,
                        "INVENTORY_NOT_FOUND"
                ));
    }


    @Transactional
    public Inventory restoreStock(Long productId, Integer qty) {

        int rows = inventoryRepository.restoreStock(productId, qty);

        if (rows == 0) {
            throw new ProductException(
                    "Failed to restore stock for product " + productId,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "STOCK_RESTORE_FAILED"
            );
        }

        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductException(
                        "Inventory not found for product " + productId,
                        HttpStatus.NOT_FOUND,
                        "INVENTORY_NOT_FOUND"
                ));
    }
}
