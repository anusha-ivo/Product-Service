package com.ordermanagement.productservice.services;

import com.ordermanagement.productservice.exceptions.ProductException;
import com.ordermanagement.productservice.dto.Inventory;
import com.ordermanagement.productservice.dto.Product;
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

public Product createProduct(Product product, Integer initialStock) {
        try {
            product.setStatus("ACTIVE");
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            Long productId = productRepository.create(product);
            product.setProductId(productId);
            inventoryRepository.createInitialStock(productId, initialStock);
            product.setAvailableQty(initialStock);

            return getProduct(productId);
        }
        catch (DuplicateKeyException ex) {
            throw new ProductException(
                    "Product with SKU '" + product.getStockKeepingUnit() + "' already exists",
                    HttpStatus.CONFLICT,
                    "DUPLICATE_PRODUCT"
            );
        }
}
    public Product getProduct(Long productId) {

        Optional<Product> optionalProduct = productRepository.findById(productId);//optional avoids null n force us to handle

        if (optionalProduct.isEmpty()) {
            throw new ProductException(
                    "Product not found with id " + productId,
                    HttpStatus.NOT_FOUND,
                    "PRODUCT_NOT_FOUND"
            );
        }

        Product product = optionalProduct.get();

        Optional<Inventory> optionalInventory =
                inventoryRepository.findByProductId(productId);

        optionalInventory.ifPresent(inventory ->
                product.setAvailableQty(inventory.getAvailableQty())
        );
        return product;

    }
    public Product updateProduct(Product product) {


        productRepository.update(product);

        return getProduct(product.getProductId());


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


}


