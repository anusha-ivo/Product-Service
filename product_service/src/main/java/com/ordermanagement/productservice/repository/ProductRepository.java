package com.ordermanagement.productservice.repository;

import com.ordermanagement.productservice.config.SqlQueryProvider;
import com.ordermanagement.productservice.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SqlQueryProvider sqlQueryProvider;

    public ProductRepository(JdbcTemplate jdbcTemplate,SqlQueryProvider sqlQueryProvider) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlQueryProvider=sqlQueryProvider;
    }

    public Long create(ProductEntity productEntity) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertQuery = sqlQueryProvider.getQuery("product.insert");

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[]{"product_id"});
            ps.setString(1, productEntity.getStockKeepingUnit());
            ps.setString(2,  productEntity.getName());
            ps.setString(3,  productEntity.getDescription());
            ps.setBigDecimal(4,  productEntity.getPrice());
            ps.setString(5,  productEntity.getCurrency());
            ps.setString(6, productEntity.getStatus());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }
    public Optional<ProductEntity> findById(Long id) {//optional to value may or may not exist,to avaid null pointer xceptions

        String findByIdQuery = sqlQueryProvider.getQuery("product.findById");
        List<ProductEntity> list = jdbcTemplate.query(findByIdQuery,
                (rs, rowNum) -> {
                    ProductEntity p = new ProductEntity();
                    p.setProductId(rs.getLong("product_id"));
                    p.setStockKeepingUnit(rs.getString("stock_keeping_unit"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setCurrency(rs.getString("currency"));
                    p.setStatus(rs.getString("status"));
                    if (rs.getTimestamp("created_at") != null) {
                        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }

                    if (rs.getTimestamp("updated_at") != null) {
                        p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    }
                    return p;
                },
                id
        );

        return list.stream().findFirst();
    }
    public int update(ProductEntity product) {
        String updateQuery = sqlQueryProvider.getQuery("product.update");
        return jdbcTemplate.update(updateQuery,
                product.getStockKeepingUnit(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCurrency(),
                product.getProductId()
        );
    }
    public int deactivate(Long id)
    {
        String deactivateQuery = sqlQueryProvider.getQuery("product.deactivate");
        return jdbcTemplate.update(deactivateQuery, id);
    }


}
