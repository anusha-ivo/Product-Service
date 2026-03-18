package com.ordermanagement.productservice.repository;

import com.ordermanagement.productservice.config.SqlQueryProvider;
import com.ordermanagement.productservice.dto.Inventory;
import com.ordermanagement.productservice.exceptions.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InventoryRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SqlQueryProvider sqlQueryProvider;

    public InventoryRepository(JdbcTemplate jdbcTemplate,SqlQueryProvider sqlQueryProvider) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlQueryProvider=sqlQueryProvider;
    }

    public void createInitialStock(Long productId, Integer qty) {
        String insertQuery = sqlQueryProvider.getQuery("inventory.insert");

        int rows = jdbcTemplate.update(insertQuery, productId, qty);

        if (rows == 0) {

            throw new ProductException(
                    "Failed to create initial stock for product " + productId,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "STOCK_CREATION_FAILED"
            );
        }
    }
    public Optional<Inventory> findByProductId(Long productId) {

        String findQuery = sqlQueryProvider.getQuery("inventory.findById");

        List<Inventory> list = jdbcTemplate.query(findQuery,
                (rs, rowNum) -> {
                    Inventory inv = new Inventory();
                    inv.setProductId(rs.getLong("product_id"));
                    inv.setAvailableQty(rs.getInt("available_qty"));
                    return inv;
                },
                productId
        );

        if (list.isEmpty()) {
            throw new ProductException(
                    "Inventory not found for product " + productId,
                    HttpStatus.NOT_FOUND,
                    "INVENTORY_NOT_FOUND"
            );
        }


        return Optional.of(list.get(0));
    }
    public int deductStock(Long productId, Integer qty) {

        String deductQuery = sqlQueryProvider.getQuery("inventory.deduct");
        int updatedRows = jdbcTemplate.update(deductQuery, qty, productId, qty);

        if (updatedRows == 0) {
            // ⚡ Throw AppException if deduction fails
            throw new ProductException(
                    "Failed to deduct " + qty + " units from product " + productId,
                    HttpStatus.BAD_REQUEST,
                    "STOCK_DEDUCTION_FAILED"
            );
        }

        return updatedRows;
    }

    public int restoreStock(Long productId, Integer qty) {
        String restoreQuery = sqlQueryProvider.getQuery("inventory.restore");
        int updatedRows = jdbcTemplate.update(restoreQuery, qty, productId);

        if (updatedRows == 0) {
            // ⚡ Throw AppException if restore fails
            throw new ProductException(
                    "Failed to restore " + qty + " units for product " + productId,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "STOCK_RESTORE_FAILED"
            );
        }

        return updatedRows;
    }
}
