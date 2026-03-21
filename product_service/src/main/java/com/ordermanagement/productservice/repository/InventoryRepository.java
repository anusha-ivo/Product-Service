package com.ordermanagement.productservice.repository;

import com.ordermanagement.productservice.config.SqlQueryProvider;
import com.ordermanagement.productservice.entity.InventoryEntity;
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

        jdbcTemplate.update(insertQuery, productId, qty);
    }
    public Optional<InventoryEntity> findByProductId(Long productId) {

        String findQuery = sqlQueryProvider.getQuery("inventory.findById");

        return jdbcTemplate.query(findQuery,
                (rs, rowNum) -> {
                    InventoryEntity inv = new InventoryEntity();
                    inv.setProductId(rs.getLong("product_id"));
                    inv.setAvailableQty(rs.getInt("available_qty"));
                    return inv;
                },
                productId
        ).stream().findFirst();
    }
    public Optional<InventoryEntity>  deductStock(Long productId, Integer qty) {

        String deductQuery = sqlQueryProvider.getQuery("inventory.deduct");
        List<InventoryEntity> list = jdbcTemplate.query(
                deductQuery,
                (rs, rowNum) -> {
                    InventoryEntity inv = new InventoryEntity();
                    inv.setProductId(rs.getLong("product_id"));
                    inv.setAvailableQty(rs.getInt("available_qty"));
                    return inv;
                },
                qty, productId, qty
        );

        return list.stream().findFirst();
    }
    public Optional<InventoryEntity> restoreStock(Long productId, Integer qty) {

        String restoreQuery = sqlQueryProvider.getQuery("inventory.restore");

        List<InventoryEntity> list = jdbcTemplate.query(
                restoreQuery,
                (rs, rowNum) -> {
                    InventoryEntity inv = new InventoryEntity();
                    inv.setProductId(rs.getLong("product_id"));
                    inv.setAvailableQty(rs.getInt("available_qty"));
                    return inv;
                },
                qty, productId
        );

        return list.stream().findFirst();
    }
}
