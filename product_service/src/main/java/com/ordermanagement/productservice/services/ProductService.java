package com.ordermanagement.productservice.services;

import com.ordermanagement.productservice.dto.ProductResponse;
import com.ordermanagement.productservice.entity.ProductEntity;
import com.ordermanagement.productservice.exceptions.ProductException;
import com.ordermanagement.productservice.dto.InventoryRequest;
import com.ordermanagement.productservice.dto.ProductRequest;
import com.ordermanagement.productservice.repository.InventoryRepository;
import com.ordermanagement.productservice.repository.ProductRepository;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    public ProductService(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }


    @Transactional
    public ProductResponse createProduct(ProductRequest request) {

        try {

            ProductEntity entity = toEntity(request);

            Long productId = productRepository.create(entity);

            inventoryRepository.createInitialStock(productId, request.getInitialStock());

            return toResponse(entity, productId, request.getInitialStock());

        } catch (DuplicateKeyException ex) {
            throw new ProductException(
                    "Product with SKU '" + request.getStockKeepingUnit() + "' already exists",
                    HttpStatus.CONFLICT,
                    "DUPLICATE_PRODUCT"
            );
        }
    }
    public ProductResponse getProduct(Long productId) {

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(
                        "Product not found with id " + productId,
                        HttpStatus.NOT_FOUND,
                        "PRODUCT_NOT_FOUND"
                ));

        Integer qty = inventoryRepository.findByProductId(productId)
                .map(i -> i.getAvailableQty())
                .orElse(0);

        return toResponse(product, productId, qty);


    }
    public ProductResponse updateProduct(Long productId, ProductRequest request) {

        ProductEntity entity = toEntity(request);
        entity.setProductId(productId);
        entity.setUpdatedAt(LocalDateTime.now());

        int rows = productRepository.update(entity);

        if (rows == 0) {
            throw new ProductException(
                    "Product not found with id " + productId,
                    HttpStatus.NOT_FOUND,
                    "PRODUCT_NOT_FOUND"
            );
        }

        return getProduct(productId);
    }

    public void deactivateProduct(Long productId) {

        int rows = productRepository.deactivate(productId);

        if (rows == 0) {
            throw new ProductException(
                    "Product not found with id " + productId,
                    HttpStatus.NOT_FOUND,
                    "PRODUCT_NOT_FOUND"
            );
        }

    }
    private ProductEntity toEntity(ProductRequest request) {//request to entity
        ProductEntity entity = new ProductEntity();
        entity.setStockKeepingUnit(request.getStockKeepingUnit());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setCurrency(request.getCurrency());
        entity.setStatus("ACTIVE");
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
    private ProductResponse toResponse(ProductEntity product, Long productId, Integer qty) {//entity to response
        ProductResponse response = new ProductResponse();
        response.setProductId(productId);
        response.setStockKeepingUnit(product.getStockKeepingUnit());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCurrency(product.getCurrency());
        response.setAvailableQty(qty);
        response.setStatus(product.getStatus());
        return response;
    }


}


